/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.ghost;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "Eagle", description = "Sneaks on the edge of blocks", category = Category.LEGIT)
public final class Eagle extends Module {

    private boolean sneaking;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir
                && mc.thePlayer.onGround) {
            sneaking = true;
            mc.gameSettings.keyBindSneak.setKeyPressed(true);
        } else {
            if (sneaking) {
                mc.gameSettings.keyBindSneak.setKeyPressed(false);
                sneaking = false;
            }
        }
    }

    @Override
    protected void onDisable() {
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
            mc.gameSettings.keyBindSneak.setKeyPressed(false);
        }
    }
}
