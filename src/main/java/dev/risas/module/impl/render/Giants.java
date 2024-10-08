package dev.risas.module.impl.render;

import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.NumberSetting;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Giants", description = "Changes the size of entities", category = Category.RENDER)
public class Giants extends Module {

    private final NumberSetting size = new NumberSetting("Size", this, 1, 0.1, 10, 0.2);
}