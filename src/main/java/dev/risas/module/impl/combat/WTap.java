/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.combat;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.other.AttackEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.util.player.PacketUtil;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@ModuleInfo(name = "WTap", description = "Makes people take more knockback", category = Category.COMBAT)
public final class WTap extends Module {

    private final BooleanSetting legit = new BooleanSetting("Legit", this, false);
    public static int ticks;

    @Override
    public void onAttackEvent(final AttackEvent event) {
        ticks = 0;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        ++ticks;

        if (mc.thePlayer.isSprinting()) {
            if (legit.isEnabled()) {
                if (ticks == 2) mc.thePlayer.setSprinting(false);
                if (ticks == 3) mc.thePlayer.setSprinting(true);
            } else {
                if (ticks < 10) {
                    PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                }
            }
        }
    }
}
