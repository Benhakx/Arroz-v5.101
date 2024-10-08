/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.movement;

import dev.risas.event.impl.motion.PostMotionEvent;
import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.other.WorldChangedEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.util.player.PacketUtil;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "Sneak", description = "Sneaks for you", category = Category.MOVEMENT)
public final class Sneak extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Always", "Always", "Packet", "NCP", "Hold");

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        if (mode.is("Packet"))
            PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        switch (mode.getMode()) {
            case "Always":
                mc.thePlayer.movementInput.sneak = mc.thePlayer.ticksExisted > 20;
                break;

            case "Hold":
                mc.gameSettings.keyBindSneak.setKeyPressed(true);
                break;

            case "NCP":
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                break;
        }
    }

    @Override
    public void onPostMotion(final PostMotionEvent event) {
        switch (mode.getMode()) {
            case "NCP":
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                break;
        }
    }

    @Override
    protected void onEnable() {
        if (mode.is("Packet"))
            PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
    }

    @Override
    protected void onDisable() {
        switch (mode.getMode()) {
            case "Always":
                mc.thePlayer.movementInput.sneak = false;
                break;

            case "NCP":
            case "Packet":
                PacketUtil.sendPacket(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                break;

            case "Hold":
                mc.gameSettings.keyBindSneak.setKeyPressed(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
                break;
        }
    }
}
