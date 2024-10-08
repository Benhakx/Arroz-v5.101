package dev.risas.module.impl.other;

import dev.risas.Rise;
import dev.risas.event.impl.motion.PreMotionEvent;
import dev.risas.event.impl.packet.PacketReceiveEvent;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.notifications.NotificationType;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.ModeSetting;
import dev.risas.setting.impl.NoteSetting;
import dev.risas.setting.impl.NumberSetting;
import dev.risas.util.math.TimeUtil;
import dev.risas.util.player.PacketUtil;
import dev.risas.util.player.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

@ModuleInfo(name = "AutoHypixel", description = "Allows you to automatically do various things on Hypixel", category = Category.OTHER)
public class AutoHypixel extends Module {

    private final NoteSetting generalSettings = new NoteSetting("General Settings", this);
    private final BooleanSetting autoRejoin = new BooleanSetting("Auto Rejoin", this, true);
    private final BooleanSetting autoPlay = new BooleanSetting("Auto Play", this, true);
    private final BooleanSetting autoGG = new BooleanSetting("Auto GG", this, true);
    private final NoteSetting autoGGSettings = new NoteSetting("Auto GG Settings", this);
    private final NumberSetting sendDelay = new NumberSetting("Send Delay", this, 1000, 0, 10000, 100);
    private final BooleanSetting winOnly = new BooleanSetting("Win Only", this, true);
    private final NoteSetting autoPlaySettings = new NoteSetting("Auto Play Settings", this);
    private final ModeSetting playMode = new ModeSetting("Play Mode", this, "Skywars", "Skywars", "Bedwars");
    private final ModeSetting skywarsMode = new ModeSetting("Skywars Mode", this, "Solo", "Solo", "Doubles", "Ranked");
    private final ModeSetting skywarsType = new ModeSetting("Skywars Type", this, "Insane", "Insane", "Normal");
    private final ModeSetting bedwarsMode = new ModeSetting("Bedwars Mode", this, "Solo", "Solo", "Doubles", "Triples", "Quads");
    private final NumberSetting playDelay = new NumberSetting("Play Delay", this, 3000, 0, 10000, 100);

    private final NoteSetting safety = new NoteSetting("Safety", this);
    private final BooleanSetting dcOnBan = new BooleanSetting("Disconnect on other player ban", this, true);
    private final BooleanSetting dcOnVeloCheck = new BooleanSetting("Disconnect on velocity test", this, true);
    private final ModeSetting dcMethod = new ModeSetting("Disconnect Method", this, "Lobby", "Limbo", "Lobby");

    private final TimeUtil ggTimer = new TimeUtil();
    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil banTimer = new TimeUtil();
    private boolean sent = true, rejoin, gg = true;

    @Override
    public void onUpdateAlwaysInGui() {
        if (autoPlay.isEnabled()) {
            autoPlaySettings.hidden = false;
            playMode.hidden = false;
            skywarsMode.hidden = !playMode.is("Skywars");
            skywarsType.hidden = !playMode.is("Skywars") || skywarsMode.is("Ranked");
            bedwarsMode.hidden = !playMode.is("Bedwars");
            playDelay.hidden = false;
        } else {
            autoPlaySettings.hidden = true;
            playMode.hidden = true;
            skywarsMode.hidden = true;
            skywarsType.hidden = true;
            bedwarsMode.hidden = true;
            playDelay.hidden = true;
        }

        autoGGSettings.hidden = sendDelay.hidden = winOnly.hidden = !autoGG.isEnabled();

        this.hidden = !(PlayerUtil.isOnServer("Hypixel") || mc.isSingleplayer());
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            final S02PacketChat p = (S02PacketChat) event.getPacket();
            final String message = p.getChatComponent().getUnformattedText();

            switch (message) {
                case "You won! Want to play again? Click here! ":
                    if (autoGG.isEnabled()) {
                        ggTimer.reset();
                        gg = false;
                    }
                    timer.reset();
                    sent = false;
                    break;

                case "You lost! Want to play again? Click here! ":
                    if (autoGG.isEnabled() && !winOnly.isEnabled()) {
                        ggTimer.reset();
                        gg = false;
                    }
                    timer.reset();
                    sent = false;
                    break;

                case "You died! Want to play again? Click here! ":
                    timer.reset();
                    sent = false;
                    break;
            }

            if (autoRejoin.isEnabled() && message.startsWith("You were spawned in Limbo.")) {
                mc.thePlayer.sendChatMessage(getJoinCommand());
                rejoin = true;
            }

            if (dcOnBan.isEnabled() && message.contains("A player has been removed from your game.")) {

                banTimer.reset();

                if (dcMethod.getMode().equals("Limbo")) {
                    for (int i = 0; i <= 20; i++) {
                        PacketUtil.sendPacketWithoutEvent(new C01PacketChatMessage("/"));
                    }
                } else {
                    PacketUtil.sendPacketWithoutEvent(new C01PacketChatMessage("/l"));
                }

                Rise.INSTANCE.getNotificationManager().registerNotification("You were disconnected because staff banned a player in your game", 10000, NotificationType.WARNING);
            }

            if (message.contains("Unknown command.") && banTimer.getElapsedTime() < 5000) {
                event.setCancelled(true);
            }
        } else if (dcOnVeloCheck.isEnabled() && event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();

            // This doesn't account for bows/snowballs/eggs.

//            if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
//                if (mc.theWorld.playerEntities.stream().filter(player -> !player.isInvisible())
//                        .mapToDouble(player -> mc.thePlayer.getDistanceToEntity(player)).min().orElse(100) > 7) {
//                    if (dcMethod.getMode().equals("Limbo")) {
//                        for (int i = 0; i < 10; i++) {
//                            PacketUtil.sendPacketWithoutEvent(new C01PacketChatMessage("/"));
//                        }
//                    } else {
//                        PacketUtil.sendPacketWithoutEvent(new C01PacketChatMessage("/l"));
//                    }
//
//                    Rise.INSTANCE.getNotificationManager().registerNotification("You were disconnected because staff tested your velocity", 10000, NotificationType.WARNING);
//                }
//            }
        }
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

        if (autoRejoin.isEnabled()) {
            if (mc.thePlayer.ticksExisted > 1 && rejoin) {
                this.registerNotification("Attempted to rejoin the game.");
                mc.thePlayer.sendChatMessage("/rejoin");
                rejoin = false;
            }
        } else
            rejoin = false;

        if (autoPlay.isEnabled()) {
            if (timer.hasReached(Math.round(playDelay.getValue() + (Math.random() * 100))) && !sent) {
                mc.thePlayer.sendChatMessage(getJoinCommand());
                sent = true;
            }
        } else
            sent = true;

        if (autoGG.isEnabled() && ggTimer.hasReached(Math.round(sendDelay.getValue() + (Math.random() * 100))) && !gg) {
            mc.thePlayer.sendChatMessage("gg");
            gg = true;
        }
    }

    @Override
    protected void onEnable() {
        ggTimer.reset();
        rejoin = false;
        timer.reset();
        sent = true;
        gg = true;
    }

    private String getJoinCommand() {
        switch (playMode.getMode()) {
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

                    case "Triples":
                        return "/play bedwars_four_three";

                    case "Quads":
                        return "/play bedwars_four_four";
                }
                break;
        }
        return "/l";
    }
}