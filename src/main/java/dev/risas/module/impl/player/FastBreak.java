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
import dev.risas.setting.impl.NumberSetting;

@ModuleInfo(name = "FastBreak", description = "Breaks blocks faster", category = Category.PLAYER)
public final class FastBreak extends Module {

    private final NumberSetting speed = new NumberSetting("Speed", this, 0.5, 0, 0.9, 0.1);

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        mc.playerController.blockHitDelay = 0;

        if (mc.playerController.curBlockDamageMP > 1 - speed.getValue())
            mc.playerController.curBlockDamageMP = 1;
    }
}
