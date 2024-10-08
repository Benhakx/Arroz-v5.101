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
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.NumberSetting;

@ModuleInfo(name = "Derp", description = "Makes you look like a derp", category = Category.PLAYER)
public final class Derp extends Module {

    public static float yaw, pitch;
    private final BooleanSetting headless = new BooleanSetting("Headless", this, false);
    private final BooleanSetting random = new BooleanSetting("Random", this, false);
    private final NumberSetting constantSpeed = new NumberSetting("Constant Speed", this, 10, 10, 180, 0.1);
    private final BooleanSetting twerk = new BooleanSetting("Twerk", this, false);
    private final BooleanSetting down = new BooleanSetting("Down", this, false);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
     // funni
        if (random.isEnabled()) {
            pitch = (float) (Math.random() * 180 - 90);
            yaw = (float) Math.random() * 360;
        } else {
            pitch = mc.thePlayer.rotationPitch;
            yaw += constantSpeed.getValue();
        }
        if(down.isEnabled()) {
            pitch = (float) (80 + Math.random() / 80);
        }
        if (headless.isEnabled())
            pitch = (float) (180 + Math.random() / 100);

        event.setYaw(yaw);
        event.setPitch(pitch);
        mc.thePlayer.renderYawOffset = yaw;
        mc.thePlayer.rotationYawHead = yaw;

        if (twerk.isEnabled()) {
            mc.gameSettings.keyBindSneak.setKeyPressed(mc.thePlayer.ticksExisted % 2 == 0);
        }
    }
}
