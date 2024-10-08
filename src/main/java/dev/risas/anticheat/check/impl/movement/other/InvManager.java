package dev.risas.anticheat.check.impl.movement.other;

import dev.risas.anticheat.check.Check;
import dev.risas.anticheat.check.api.CheckInfo;
import dev.risas.anticheat.data.PlayerData;
import dev.risas.anticheat.util.PacketUtil;
import net.minecraft.network.Packet;

import javax.vecmath.Vector2d;

@CheckInfo(name = "InventoryMove", type = "A", description = "Detects suspicious actions in inventories")
public class InvManager extends Check {

    public InvManager(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> packet) {
        if (PacketUtil.isRelMove(packet)) {

            Vector2d delta = new Vector2d(data.getDeltaX(), data.getDeltaZ());
            Vector2d lastDelta = new Vector2d(data.getLastDeltaX(), data.getLastDeltaZ());

            if (delta.length() >= lastDelta.length()) {

            }
        }
    }
}
