package dev.risas.script.block.impl.variables.impl.bool;

import dev.risas.script.block.impl.variables.VariableBlock;

public final class OnGroundVariable extends VariableBlock {

    @Override
    public <T> T get() {
        return (T) (Boolean) mc.thePlayer.onGround;
    }
}
