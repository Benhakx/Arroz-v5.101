package dev.risas.module.impl.render;

import dev.risas.Rise;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "SocialCreditScoreESP", description = "Gives people social credit score like in China", category = Category.RENDER)
public class SocialCreditScoreESP extends Module {

    @Override
    protected void onEnable() {
        Rise.addChatMessage("SocialCreditScoreESP may lag on large servers");
    }
}
