package dev.risas.module.impl.render;

import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "DiscordRPC", description = "Shows that you're on Rise on Discord", category = Category.RENDER)
public class DiscordRPC extends Module {
    public BooleanSetting showServerIp = new BooleanSetting("Show Server IP", this, true);
}
