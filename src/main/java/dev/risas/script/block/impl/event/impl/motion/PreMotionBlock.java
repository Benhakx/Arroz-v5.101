package dev.risas.script.block.impl.event.impl.motion;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.script.block.api.BlockData;
import dev.risas.script.block.impl.event.EventBlock;

@BlockData(name = "onPreMotion", description = "Instructions inside this event listener will get ran when the pre motion event is called.")
public final class PreMotionBlock extends EventBlock {

    public PreMotionBlock() {
        super(PreMotionEvent.class);
    }
}
