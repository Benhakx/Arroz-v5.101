/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.render;

import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.NumberSetting;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Enchant", description = "Changes what enchantments look like on items", category = Category.RENDER)
public final class Enchant extends Module {

    private final NumberSetting strength = new NumberSetting("Strength", this, 1, 1, 10, 1);
    private final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, true);
    private final NumberSetting hue = new NumberSetting("Hue", this, 1, 1, 255, 10);

    @Override
    public void onUpdateAlwaysInGui() {

        hue.hidden = rainbow.isEnabled();

    }
}
