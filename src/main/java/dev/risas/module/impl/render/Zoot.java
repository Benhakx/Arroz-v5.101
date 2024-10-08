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
import dev.risas.notifications.Notification;
import dev.risas.setting.impl.ModeSetting;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Zoot", description = "Removes bad visual potion effects.", category = Category.RENDER)
public final class Zoot extends Module {


    @Override
    public void onPreMotion(final PreMotionEvent event) {
                if(mc.thePlayer.isPotionActive(Potion.blindness)){
                    mc.thePlayer.removePotionEffect(15);
                    this.registerNotification("Remove Blindess Effect!");
                }
                if(mc.thePlayer.isPotionActive(Potion.confusion)){
                    mc.thePlayer.removePotionEffect(9);
                    this.registerNotification("Remove Nausea Effect!");
                }
    }
}
