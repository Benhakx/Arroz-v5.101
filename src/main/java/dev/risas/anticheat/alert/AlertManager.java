package dev.risas.anticheat.alert;

import dev.risas.anticheat.check.Check;
import dev.risas.module.impl.other.AntiCheat;
import dev.risas.util.InstanceAccess;
import dev.risas.util.player.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

public final class AlertManager implements InstanceAccess {

    private final Minecraft mc = Minecraft.getMinecraft();

    public void sendAlert(final Check check) {
        final String base = "§dRise §7» §d%player%§7 has failed §d%check% §7(§d%type%§7)%dev% (§dx%vl%§7)"
                .replaceAll("%player%", check.getData().getPlayer().getName())
                .replaceAll("%check%", check.getCheckInfo().name())
                .replaceAll("%type%", check.getCheckInfo().type())
                .replaceAll("%dev%", "")
                .replaceAll("%vl%", String.valueOf(check.getViolations()));

        final NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        final NetworkPlayerInfo targetInfo = mc.getNetHandler().getPlayerInfo(check.getData().getPlayer().getUniqueID());

        final int ping = playerInfo == null || mc.isSingleplayer() ? 0 : playerInfo.getResponseTime();
        final int targetPing = targetInfo == null || mc.isSingleplayer() ? 0 : targetInfo.getResponseTime();

        final String hover = "§dDescription: §f" + check.getCheckInfo().description().concat("\n")
                .concat("§dYour Ping: " + ping).concat("\n")
                .concat("§dTheir Ping: " + targetPing);

        final ChatStyle hoverStyle = new ChatStyle().setChatHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hover))
        );

        switch (this.getMode(AntiCheat.class, "Alerts")) {
            case "Client":
                mc.thePlayer.addChatMessage(new ChatComponentText(base).setChatStyle(hoverStyle));
                break;

            case "Server":
                PacketUtil.sendPacketWithoutEvent(new C01PacketChatMessage("Rise > %player% has failed %check% (%type%)%dev% (x%vl%)"
                        .replaceAll("%player%", check.getData().getPlayer().getName())
                        .replaceAll("%check%", check.getCheckInfo().name())
                        .replaceAll("%type%", check.getCheckInfo().type())
                        .replaceAll("%dev%", "")
                        .replaceAll("%vl%", String.valueOf(check.getViolations()))));
                break;
        }
    }
}
