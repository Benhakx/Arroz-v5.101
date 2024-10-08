/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.movement;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.other.MoveButtonEvent;
import dev.risas.event.impl.other.MoveEvent;
import dev.risas.event.impl.packet.PacketReceiveEvent;
import dev.risas.event.impl.render.Render2DEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.util.player.PacketUtil;
import dev.risas.util.player.PlayerUtil;
import dev.risas.util.render.theme.ThemeType;
import dev.risas.util.render.theme.ThemeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.awt.*;

@ModuleInfo(name = "Clipper", description = "Clips you down when you sneak", category = Category.MOVEMENT)
public final class Clipper extends Module {

    private boolean triggered, available, clipping;
    private int amount;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        available = false;

        boolean lastAir = false;
        for (int i = 0; i < 10; i++) {
            final Block block = PlayerUtil.getBlockRelativeToPlayer(0, -i, 0);

            if (block instanceof BlockAir) {
                if (lastAir && !(PlayerUtil.getBlockRelativeToPlayer(0, -i - 1, 0) instanceof BlockAir) && !(PlayerUtil.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockAir) && mc.thePlayer.onGround) {
                    available = true;
                    if (!triggered && mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - i, mc.thePlayer.posZ);
                        clipping = false;
                        triggered = true;
                    }
                }
                lastAir = true;
            } else
                lastAir = false;
        }

        if (mc.thePlayer.ticksExisted == 1)
            clipping = false;

        if (clipping)
            event.setY(event.getY() - amount);

        if (!mc.gameSettings.keyBindSneak.isKeyDown())
            triggered = false;
    }

    @Override
    public void onMove(final MoveEvent event) {
        if (clipping)
            event.setCancelled(true);
    }

    @Override
    public void onMoveButton(final MoveButtonEvent event) {
        if (available)
            event.setSneak(false);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();

        if (p instanceof S08PacketPlayerPosLook && clipping) {
            final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;
            if (mc.thePlayer.posY - packet.getY() >= amount - 1 || packet.getY() > mc.thePlayer.posY)
                clipping = false;
            else
                event.setCancelled(true);
        }
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);

        if (available && mc.thePlayer.onGround) {
            final String name = "Clipper usage available, sneak to activate.";
            this.comfortaa.drawCenteredString(name, scaledResolution.getScaledWidth() / 2F, scaledResolution.getScaledHeight() - 89.5F, new Color(0, 0, 0, 200).hashCode());
            this.comfortaa.drawCenteredString(name, scaledResolution.getScaledWidth() / 2F, scaledResolution.getScaledHeight() - 90, ThemeUtil.getThemeColor(ThemeType.GENERAL).hashCode());
        }
    }

    @Override
    protected void onEnable() {
        clipping = false;
    }
}