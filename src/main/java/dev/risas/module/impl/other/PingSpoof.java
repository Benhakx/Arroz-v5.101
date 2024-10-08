package dev.risas.module.impl.other;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.other.WorldChangedEvent;
import dev.risas.event.impl.packet.PacketReceiveEvent;
import dev.risas.event.impl.packet.PacketSendEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.math.TimeUtil;
import dev.risas.util.player.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ModuleInfo(name = "PingSpoof", description = "Makes your ping higher for the server", category = Category.OTHER)
public final class PingSpoof extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", this, 1000, 10, 30000, 1);

    private final ConcurrentHashMap<Packet<?>, Long> packets = new ConcurrentHashMap<>();

    private final TimeUtil timer = new TimeUtil();

    @Override
    protected void onDisable() {
        packets.clear();
    }

    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        if (mc.isSingleplayer()) return;

        packets.clear();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.isSingleplayer())
            return;

        for (final Iterator<Map.Entry<Packet<?>, Long>> iterator = packets.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Packet<?>, Long> entry = iterator.next();

            if (entry.getValue() < System.currentTimeMillis()) {
                PacketUtil.sendPacket(entry.getKey());
                iterator.remove();
            }
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mc.isSingleplayer())
            return;

        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            timer.reset();
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (mc.isSingleplayer())
            return;

        final Packet<?> p = event.getPacket();

        if (p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive) {
            packets.put(p, (long) (System.currentTimeMillis() + delay.getValue()));
            event.setCancelled(true);
        }
    }
}