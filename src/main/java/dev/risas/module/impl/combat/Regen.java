package dev.risas.module.impl.combat;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.player.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Regen", description = "Regenerates your health super fast by spamming position packets", category = Category.COMBAT)
public class Regen extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "C03", "C03", "C04", "C05", "C06");
    private final NumberSetting health = new NumberSetting("Health", this, 15, 0, 20, 1);
    private final NumberSetting speed = new NumberSetting("Speed", this, 20, 1, 300, 1);
    private final NumberSetting tick = new NumberSetting("Tick", this, 1, 1, 20, 1);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.ticksExisted > 10 && mc.thePlayer.getHealth() < health.getValue() && mc.thePlayer.ticksExisted % tick.getValue() == 0) {
            for (int i = 0; i < speed.getValue(); i++) {
                switch (mode.getMode()) {
                    case "C03":
                        PacketUtil.sendPacket(new C03PacketPlayer(mc.thePlayer.onGround));
                        break;

                    case "C04":
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
                        break;

                    case "C05":
                        PacketUtil.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
                        break;

                    case "C06":
                        PacketUtil.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
                        break;
                }
            }
        }
    }
}
