/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.other;

import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.math.TimeUtil;
import dev.risas.util.player.PlayerUtil;
import net.minecraft.client.network.NetworkPlayerInfo;

@ModuleInfo(name = "Sniper", description = "Snipes the given user in the given game on Hypixel", category = Category.OTHER)
public final class Sniper extends Module {

    public static String username = "Rise";

    private final ModeSetting mode = new ModeSetting("Mode", this, "Bedwars", "Skywars", "Bedwars");
    private final ModeSetting skywarsMode = new ModeSetting("Skywars Mode", this, "Solo", "Solo", "Doubles");
    private final ModeSetting skywarsType = new ModeSetting("Skywars Type", this, "Normal", "Normal", "Insane", "Ranked");
    private final ModeSetting bedwarsMode = new ModeSetting("Bedwars Mode", this, "Solo", "Solo", "Doubles", "Threes", "Fours", "4v4");
    private final NumberSetting delay = new NumberSetting("Delay", this, 1000, 50, 10000, 100);

    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onUpdateAlwaysInGui() {
        bedwarsMode.hidden = !mode.is("Bedwars");

        skywarsMode.hidden = !(mode.is("Skywars") && !skywarsType.is("Ranked"));

        skywarsType.hidden = !mode.is("Skywars");

        this.hidden = !(PlayerUtil.isOnServer("Hypixel") || mc.isSingleplayer());
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (!PlayerUtil.isOnServer("Hypixel") && !mc.isSingleplayer()) {
            this.registerNotification(this.getModuleInfo().name() + " only works on Hypixel.");
            this.toggleModule();
            return;
        }

        if (mc.isSingleplayer())
            return;

        final long delay = Math.round(this.delay.getValue() + (Math.random() * 100));

        for (final NetworkPlayerInfo info : mc.thePlayer.sendQueue.getPlayerInfoMap()) {
            if (info.getGameProfile().getName() == null)
                continue;

            final String name = info.getGameProfile().getName();
            if (name.equalsIgnoreCase(username)) {
                this.registerNotification("Successfully sniped " + username + ".");
                this.toggleModule();
                return;
            }
        }

        if (timer.hasReached(delay)) {
            mc.thePlayer.sendChatMessage(getJoinCommand());
            timer.reset();
        }
    }

    private String getJoinCommand() {
        switch (mode.getMode()) {
            case "Skywars":
                switch (skywarsMode.getMode()) {
                    case "Solo":
                        return "/play solo_" + skywarsType.getMode().toLowerCase();

                    case "Doubles":
                        return "/play teams_" + skywarsType.getMode().toLowerCase();

                    case "Ranked":
                        return "/play ranked_normal";
                }
                break;

            case "Bedwars":
                switch (bedwarsMode.getMode()) {
                    case "Solo":
                        return "/play bedwars_eight_one";
                    case "Doubles":
                        return "/play bedwars_eight_two";
                    case "Threes":
                        return "/play bedwars_four_three";
                    case "Fours":
                        return "/play bedwars_four_four";
                    case "4v4":
                        return "/play bedwars_two_four";
                }
                break;
        }
        return "/l";
    }
}