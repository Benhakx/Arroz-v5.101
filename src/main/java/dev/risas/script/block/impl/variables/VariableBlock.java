package dev.risas.script.block.impl.variables;

import dev.risas.script.block.Block;

public abstract class VariableBlock extends Block {
    public abstract <T> T get();
}
