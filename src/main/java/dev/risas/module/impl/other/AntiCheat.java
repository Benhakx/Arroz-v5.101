/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.other;

import dev.risas.Rise;
import dev.risas.anticheat.data.PlayerData;
import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.ModeSetting;

@ModuleInfo(name = "AntiCheat", description = "An AntiCheat built into Rise", category = Category.OTHER)
public final class AntiCheat extends Module {
    private final ModeSetting alerts = new ModeSetting("Alerts", this, "Client", "Client", "Server");

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        Rise.INSTANCE.getAntiCheat().incrementTick();
    }
}
