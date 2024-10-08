package dev.risas.module.impl.render;

import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Scoreboard", description = "Changes the look of the scoreboard", category = Category.RENDER)
public class Scoreboard extends Module {

    public static Scoreboard instance;

    public final BooleanSetting hideScoreboardSetting = new BooleanSetting("Hide Scoreboard", this, false);
    public final BooleanSetting hideBackgroundSetting = new BooleanSetting("Hide Background", this, true);
    public final BooleanSetting drawOnLeft = new BooleanSetting("Draw On Left", this, false);
    public final BooleanSetting customFont = new BooleanSetting("Custom Font", this, false);
    public final BooleanSetting customScoreboard = new BooleanSetting("Custom Scoreboard", this, false);

    public Scoreboard() {
        instance = this;
    }
}
