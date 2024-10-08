/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.combat;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.ModeSetting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "AntiBot", description = "Prevents you from attacking Aura bots", category = Category.COMBAT)
public final class AntiBot extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Custom", "Custom", "Hypixel");
    private final BooleanSetting noMoveCheck = new BooleanSetting("No Move Check", this, false);
    private final BooleanSetting duplicateNameCheck = new BooleanSetting("Duplicate Name Check", this, false);
    private final BooleanSetting ticksExistedCheck = new BooleanSetting("Ticks Existed Check", this, false);
    private final BooleanSetting ticksVisibleCheck = new BooleanSetting("Ticks Visible Check", this, false);
    private final BooleanSetting invalidNameCheck = new BooleanSetting("Invalid Name Check", this, false);
    private final BooleanSetting duplicateIDCheck = new BooleanSetting("Duplicate ID Check", this, false);
    private final BooleanSetting negativeIDCheck = new BooleanSetting("Negative ID Check", this, false);
    private final BooleanSetting distanceCheck = new BooleanSetting("Distance Check", this, false);
    private final BooleanSetting pingCheck = new BooleanSetting("Ping Check", this, false);

    public static List<Entity> bots = new ArrayList<>();

    @Override
    public void onUpdateAlwaysInGui() {
        duplicateNameCheck.hidden = ticksVisibleCheck.hidden = ticksExistedCheck.hidden = invalidNameCheck.hidden = duplicateIDCheck.hidden = negativeIDCheck.hidden = distanceCheck.hidden = pingCheck.hidden = !mode.is("Custom");
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        bots.clear();

        final List<String> names = new ArrayList<>();
        final List<Integer> ids = new ArrayList<>();

        for (final EntityPlayer player : mc.theWorld.playerEntities) {
            if (player != mc.thePlayer) {
                switch (mode.getMode()) {
                    case "Custom": {
                        if (ticksVisibleCheck.isEnabled()) {
                            if ((player.ticksExisted <= 160 || player.ticksVisible <= 160)) {
                                player.bot = true;

                                if (player.isInvisibleToPlayer(mc.thePlayer) || player.isInvisible())
                                    player.ticksVisible = 0;
                                else
                                    player.ticksVisible++;
                            } else
                                player.bot = false;
                        }

                        if (noMoveCheck.isEnabled()) {
                            if ((player.ticksExisted <= 10)) {
                                player.bot = true;
                            } else if (player.lastTickPosX != player.posX || player.lastTickPosZ != player.posZ) {
                                player.bot = false;
                            }
                        }

                        if (duplicateNameCheck.isEnabled()) {
                            final String name = player.getName();

                            if (names.contains(name))
                                player.bot = true;

                            names.add(name);
                        }

                        if (invalidNameCheck.isEnabled()) {
                            final String valid = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_";
                            final String name = player.getName();

                            for (int i = 0; i < name.length(); i++) {
                                final String c = String.valueOf(name.charAt(i));
                                if (!valid.contains(c)) {
                                    player.bot = true;
                                    break;
                                }
                            }
                        }

                        if (ticksExistedCheck.isEnabled() && player.ticksExisted <= 0)
                            player.bot = true;

                        if (duplicateIDCheck.isEnabled()) {
                            final int id = player.getEntityId();

                            if (ids.contains(id))
                                player.bot = true;

                            ids.add(id);
                        }

                        if (negativeIDCheck.isEnabled() && player.getEntityId() < 0)
                            player.bot = true;

                        if (distanceCheck.isEnabled()) {
                            if (mc.thePlayer.getDistanceSq(player.posX, mc.thePlayer.posY, player.posZ) > 200)
                                player.bot = false;

                            if (player.ticksExisted < 5) player.bot = true;
                        }

                        if (pingCheck.isEnabled()) {
                            final NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(player.getUniqueID());

                            if (info != null && info.getResponseTime() < 0)
                                player.bot = true;
                        }
                        break;
                    }

                    case "Hypixel": {
                        if (player.isInvisible() && player.onGround) {
                            if ((player.posY - player.lastTickPosY) > 1.5) {
                                player.bot = true;
                            }

                            if ((player.posY - mc.thePlayer.posY) > 4 && (player.posY - mc.thePlayer.posY) < 10 && (player.posY - player.lastTickPosY) < 0.5) {
                                player.bot = true;
                            }
                        }
                        break;
                    }
                }

                if (player.bot)
                    bots.add(player);
            }
        }
    }

    @Override
    protected void onDisable() {
        bots.clear();

        for (final Entity e : mc.theWorld.playerEntities)
            e.bot = false;
    }

    @Override
    protected void onEnable() {
        bots.clear();

        for (final Entity e : mc.theWorld.playerEntities)
            e.bot = true;
    }
}