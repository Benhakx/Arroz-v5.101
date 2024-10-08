package dev.risas.module.impl.movement;

import dev.risas.event.impl.other.StrafeEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.player.MoveUtil;

@ModuleInfo(name = "Strafe", description = "Makes you always strafe", category = Category.MOVEMENT)
public class Strafe extends Module {
    private final BooleanSetting only_on_ground = new BooleanSetting("Only On Ground", this, false);

    @Override
    public void onStrafe(final StrafeEvent event) {
        if(only_on_ground.enabled){
            if(mc.thePlayer.onGround){
                MoveUtil.strafe();
            }
        }else{
            MoveUtil.strafe();
        }
    }
}
