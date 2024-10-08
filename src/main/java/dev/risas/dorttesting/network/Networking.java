package dev.risas.dorttesting.network;

import com.google.common.collect.Lists;
import dev.risas.dorttesting.network.packet.MappedPacket;
import dev.risas.dorttesting.network.packet.packets.client.player.ClientPlayerPosition;

import java.util.List;

public class Networking {

    private final List<MappedPacket> mappedPacketList = Lists.newArrayList();

    public Networking() {
        mappedPacketList.add(new ClientPlayerPosition());
    }

    public List<MappedPacket> getMappedPacketList() {
        return mappedPacketList;
    }

}
