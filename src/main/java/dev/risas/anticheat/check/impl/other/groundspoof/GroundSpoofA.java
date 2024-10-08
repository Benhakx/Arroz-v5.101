package dev.risas.anticheat.check.impl.other.groundspoof;

import dev.risas.anticheat.check.Check;
import dev.risas.anticheat.check.api.CheckInfo;
import dev.risas.anticheat.data.PlayerData;
import dev.risas.anticheat.util.PacketUtil;
import dev.risas.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;

@CheckInfo(name = "GroundSpoof", type = "A", description = "Detects invalid ground state in air")
public final class GroundSpoofA extends Check {

    public GroundSpoofA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet<?> packet) {
        if (PacketUtil.isRelMove(packet) && !PlayerUtil.isOnServer("Hypixel")) { // Disables the check on Hypixel since ground states are hidden on there.
            final S14PacketEntity wrapper = ((S14PacketEntity) packet);

            if (data.getPlayer().getEntityId() != wrapper.entityId) return;

            final double x = data.getX();
            final double y = data.getY();
            final double z = data.getZ();

            final boolean server = !(PlayerUtil.getBlock(x - 0.5, y - 0.52, z - 0.5) instanceof BlockAir)
                    || !(PlayerUtil.getBlock(x + 0.5, y - 0.52, z - 0.5) instanceof BlockAir)
                    || !(PlayerUtil.getBlock(x + 0.5, y - 0.52, z + 0.5) instanceof BlockAir)
                    || !(PlayerUtil.getBlock(x - 0.5, y - 0.52, z + 0.5) instanceof BlockAir);

            final boolean ground = wrapper.onGround && !server && y % 0.5 != 0.0;

            if (ground) {
                if (increaseBuffer() > 2) {
                    this.fail();
                }
            } else {
                decreaseBufferBy(0.1);
            }
        }
    }
}
