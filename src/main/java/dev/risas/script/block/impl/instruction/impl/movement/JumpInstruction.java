package dev.risas.script.block.impl.instruction.impl.movement;

import dev.risas.script.block.api.BlockData;
import dev.risas.script.block.impl.instruction.InstructionBlock;

@BlockData(name = "Jump", description = "Invokes the jump method of the player entity")
public final class JumpInstruction extends InstructionBlock {

    @Override
    public void run() {
        mc.thePlayer.jump();
    }
}
