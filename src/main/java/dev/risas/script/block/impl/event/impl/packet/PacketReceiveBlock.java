package dev.risas.script.block.impl.event.impl.packet;

import dev.risas.event.impl.packet.PacketReceiveEvent;
import dev.risas.script.block.api.BlockData;
import dev.risas.script.block.impl.event.EventBlock;

@BlockData(name = "onPacketReceive", description = "Instructions inside this event listener will get ran when the client receives a packet.")
public final class PacketReceiveBlock extends EventBlock {

    public PacketReceiveBlock() {
        super(PacketReceiveEvent.class);
    }
}
