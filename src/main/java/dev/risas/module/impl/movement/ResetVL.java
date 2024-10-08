/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.movement;

import dev.risas.event.impl.motion.PostMotionEvent;
import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.packet.PacketReceiveEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.math.MathUtil;
import dev.risas.util.math.TimeUtil;
import dev.risas.util.player.MoveUtil;
import dev.risas.util.player.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Resets your violation level!
 * <p>
 * Crazy
 */
@ModuleInfo(name = "ResetVL", description = "Resets your violation level!", category = Category.MOVEMENT)
public final class ResetVL extends Module {
    private final NumberSetting Timer = new NumberSetting("Timer", this, 1, 1, 2.25, 0.05);
    private int ticks;
    private int jump;
    private double y;

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1;
        jump = 0;
        ticks = 0;
        y = 0;
    }
    @Override
    protected void onEnable() {
        mc.timer.timerSpeed = 1;
        jump = 0;
        ticks = 0;
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        mc.thePlayer.cameraYaw = 0;
        mc.thePlayer.cameraPitch = 0;
        if(mc.thePlayer.onGround){
            if(jump <= 25) {
                mc.thePlayer.motionY = 0.11 + Math.random() / 1000f;
                mc.timer.timerSpeed = (float) Timer.getValue();
                jump++;
            }else{
                mc.timer.timerSpeed = 1;
                this.toggleModule();
            }
        }else{
        }
    }

    @Override
    public void onPostMotion(final PostMotionEvent event) {
    }
}
