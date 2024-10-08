package dev.risas.anticheat.check.impl.combat.autoclicker;

import dev.risas.anticheat.check.Check;
import dev.risas.anticheat.check.api.CheckInfo;
import dev.risas.anticheat.data.PlayerData;
import dev.risas.anticheat.util.PacketUtil;
import dev.risas.util.math.TimeUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;

@CheckInfo(name = "AutoClicker", type = "A", description = "Detects clicking too fast")
public final class AutoClickerA extends Check {

    private final TimeUtil timer = new TimeUtil();
    private int swings;

    public AutoClickerA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet<?> packet) {
        if (packet instanceof S0BPacketAnimation) {
            final S0BPacketAnimation wrapper = (S0BPacketAnimation) packet;
            if (wrapper.getEntityID() == data.getPlayer().getEntityId())
                swings++;
        }

        if (PacketUtil.isRelMove(packet)) {
            if (swings > 3) {
                if (getBuffer() >= 3)
                    fail();

                increaseBuffer();
                timer.reset();
            } else if (timer.hasReached(1000L)) {
                decreaseBuffer();
                timer.reset();
            }

            swings = 0;
        }
    }
}
