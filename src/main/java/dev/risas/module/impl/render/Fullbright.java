/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.render;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.ModeSetting;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Fullbright", description = "Gives your game full brightness", category = Category.RENDER)
public final class Fullbright extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Gamma", "Gamma", "Potion");

    private float oldGamma;

    @Override
    protected void onDisable() {
        switch (mode.getMode()) {
            case "Gamma":
                mc.gameSettings.gammaSetting = oldGamma;
                break;

            case "Potion":
                mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
                break;
        }
    }

    @Override
    protected void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        switch (mode.getMode()) {
            case "Gamma":
                mc.gameSettings.gammaSetting = 100;
                break;

            case "Potion":
                if(mc.thePlayer.isPotionActive(Potion.blindness)){
                    mc.thePlayer.removePotionEffect(15);
                }
                if(mc.thePlayer.isPotionActive(Potion.confusion)){
                    mc.thePlayer.removePotionEffect(9);
                }
                mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), Integer.MAX_VALUE));
                break;
        }
    }
}
