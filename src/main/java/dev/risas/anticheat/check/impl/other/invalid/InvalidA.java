package dev.risas.anticheat.check.impl.other.invalid;

import dev.risas.anticheat.check.Check;
import dev.risas.anticheat.check.api.CheckInfo;
import dev.risas.anticheat.data.PlayerData;
import net.minecraft.network.Packet;

@CheckInfo(name = "Invalid", type = "A", description = "Detects invalid rotations")
public final class InvalidA extends Check {

    public InvalidA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet<?> packet) {
        if (Math.abs(getData().getPlayer().rotationPitch) > 90)
            fail();
    }
}
