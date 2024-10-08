package dev.risas.module.impl.movement;

import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;

@ModuleInfo(name = "SafeWalk", description = "Prevents you from walking off blocks", category = Category.MOVEMENT)
public class SafeWalk extends Module {
    private final BooleanSetting dirCheck = new BooleanSetting("Directional Check", this, false);
}
