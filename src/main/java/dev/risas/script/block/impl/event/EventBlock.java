package dev.risas.script.block.impl.event;

import dev.risas.event.api.Event;
import dev.risas.script.block.Block;
import dev.risas.script.block.impl.instruction.InstructionBlock;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class EventBlock extends Block {

    protected final List<InstructionBlock> listeners = new ArrayList<>();
    private final Class<? extends Event> clazz;

    public EventBlock(final Class<? extends Event> clazz) {
        this.clazz = clazz;
    }

    public void run(final Event event) {
        this.listeners.forEach(InstructionBlock::run);
    }
}
