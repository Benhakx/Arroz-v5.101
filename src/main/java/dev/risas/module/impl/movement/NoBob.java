package dev.risas.module.impl.movement;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;

@ModuleInfo(name = "NoBob", description = "bobbing? NO!", category = Category.MOVEMENT)
public final class NoBob extends Module {

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        mc.thePlayer.distanceWalkedModified = 0.0F;
        mc.gameSettings.viewBobbing = true;
    }
}