/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.other;

import dev.risas.event.impl.other.UpdateEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.util.player.PacketUtil;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleInfo(name = "AntiSuffocation", description = "Prevents you from being suffocated", category = Category.OTHER)
public final class AntiSuffocation extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Mine", "Mine", "VClip");
    private final BooleanSetting swing = new BooleanSetting("Swing", this, true);

    @Override
    public void onUpdateAlwaysInGui() {
        swing.hidden = !mode.is("Mine");
    }

    @Override
    public void onUpdate(final UpdateEvent event) {
        if (mc.thePlayer.isEntityInsideOpaqueBlock())
            switch (mode.getMode()) {
                case "Mine": {
                    final BlockPos downPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, downPos, EnumFacing.NORTH));
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, downPos, EnumFacing.NORTH));
                    if (swing.isEnabled())
                        mc.thePlayer.swingItem();
                    break;
                }

                case "VClip": {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ);
                    break;
                }
            }
    }
}