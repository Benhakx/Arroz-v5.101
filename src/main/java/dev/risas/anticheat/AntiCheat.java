package dev.risas.anticheat;

import dev.risas.anticheat.alert.AlertManager;
import dev.risas.anticheat.check.manager.CheckManager;
import dev.risas.anticheat.data.PlayerData;
import dev.risas.anticheat.listener.RegistrationListener;
import lombok.Getter;
import net.minecraft.network.Packet;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class AntiCheat {

    private final Map<UUID, PlayerData> playerMap = new ConcurrentHashMap<>();

    private final RegistrationListener registrationListener = new RegistrationListener();
    private final AlertManager alertManager = new AlertManager();

    public AntiCheat() {
        CheckManager.setup();
    }

    public void handle(final Packet<?> packet) {
        this.playerMap.values().forEach(playerData -> playerData.handle(packet));
    }

    public void incrementTick() {
        for (PlayerData data : playerMap.values()) {
            data.incrementTick();
        }
    }
}
