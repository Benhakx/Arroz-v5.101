package dev.risas.event.impl.packet;

import dev.risas.event.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

@Getter
@Setter
@AllArgsConstructor
public final class PacketSendEvent extends Event {
    private Packet<?> packet;
}
