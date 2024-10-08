package dev.risas.script.block.impl.variables.impl.number;

import dev.risas.script.block.api.BlockData;
import dev.risas.script.block.impl.variables.VariableBlock;

@BlockData(name = "MotionY", description = "This variable returns the motion y for the player entity.")
public final class MotionYBlock extends VariableBlock {

    @Override
    public <T> T get() {
        return (T) (Double) mc.thePlayer.motionY;
    }
}
