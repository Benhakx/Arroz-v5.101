package dev.risas.module.impl.movement;

import dev.risas.event.impl.packet.PacketSendEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.util.player.PlayerUtil;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name = "Terrain", description = "Prevents certain blocks from slowing you down", category = Category.MOVEMENT)
public class Terrain extends Module {
    private final BooleanSetting ladder = new BooleanSetting("Ladders", this, true);
    private final BooleanSetting web = new BooleanSetting("Webs", this, true);
    private final BooleanSetting soulSand = new BooleanSetting("Soul Sand", this, true);
    private final BooleanSetting vines = new BooleanSetting("Vines", this, true);
    private final BooleanSetting slime = new BooleanSetting("Slime", this, true);
}
