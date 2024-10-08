/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.player;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.player.PacketUtil;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "FastUse", description = "Comer rapido KJJ.", category = Category.PLAYER)
public final class FastEat extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "C03", "C03", "C04", "C05", "C06");
    private final NumberSetting speed = new NumberSetting("Speed", this, 20, 1, 100, 1);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
         if (mc.thePlayer.isEating() || mc.thePlayer.isUsingItem() && !this.getModule(Scaffold.class).isEnabled()) {
            for (int i = 0; i < (int) (speed.getValue() / 2); i++) {
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
}
