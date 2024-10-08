/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.other;

import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;

@ModuleInfo(name = "NameProtect", description = "Hides your name", category = Category.RENDER)
public final class NameProtect extends Module {
    public static boolean enabled;
    // FontRenderer class


    @Override
    protected void onEnable() {
        enabled = true;
    }

    @Override
    protected void onDisable() {
        enabled = false;
    }

}
