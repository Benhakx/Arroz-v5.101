package dev.risas.script.block.impl.event.impl.packet;

import dev.risas.event.impl.packet.PacketSendEvent;
import dev.risas.script.block.api.BlockData;
import dev.risas.script.block.impl.event.EventBlock;

@BlockData(name = "onPacketSend", description = "Instructions inside this event listener will get ran when the client sends a packet.")
public final class PacketSendBlock extends EventBlock {

    public PacketSendBlock() {
        super(PacketSendEvent.class);
    }
}
