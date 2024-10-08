package dev.risas.dorttesting.network.packet;

import dev.risas.dorttesting.network.packet.mapping.PacketMapping;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

public interface MappedPacket extends Packet<INetHandler> {

    PacketMapping getMapping();

}
