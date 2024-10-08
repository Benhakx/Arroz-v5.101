package dev.risas.module.impl.other;

import dev.risas.Rise;
import dev.risas.event.impl.packet.PacketReceiveEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

@ModuleInfo(name = "Debugger", description = "Debugs packets", category = Category.OTHER)
public final class Debugger extends Module {

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S32PacketConfirmTransaction) {
            final S32PacketConfirmTransaction wrapper = ((S32PacketConfirmTransaction) packet);

            Rise.addChatMessage("Transaction | id: " + wrapper.getActionNumber());
        }

        if (packet instanceof S00PacketKeepAlive) {
            final S00PacketKeepAlive wrapper = ((S00PacketKeepAlive) packet);

            Rise.addChatMessage("KeepAlive | id: " + wrapper.func_149134_c());
        }
    }
}