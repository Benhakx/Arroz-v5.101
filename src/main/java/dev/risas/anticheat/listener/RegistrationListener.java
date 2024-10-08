package dev.risas.anticheat.listener;

import dev.risas.Rise;
import dev.risas.anticheat.data.PlayerData;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public final class RegistrationListener {

    public void handleSpawn(final EntityOtherPlayerMP entity) {
        Rise.INSTANCE.getAntiCheat().getPlayerMap().put(entity.getUniqueID(), new PlayerData(entity));
    }

    /**
     * Actually destroy the entities as we yknow don't want memory
     * leaks in the client due to a retarded anticheat base lmao.
     */
    public void handleDestroy(final UUID uuid) {
        Rise.INSTANCE.getAntiCheat().getPlayerMap().remove(uuid);
    }
}
