package dev.risas.command.impl;

import dev.risas.Rise;
import dev.risas.command.Command;
import dev.risas.command.api.CommandInfo;
import dev.risas.notifications.NotificationType;

@CommandInfo(name = "Spammer", description = "Sets the custom spammer message", syntax = ".spammer <message>", aliases = {"spammer", "spam"})
public final class Spammer extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.contains("loyisa.cn")) {
            Rise.INSTANCE.getNotificationManager().registerNotification("This server is in a Spammer blacklist due to a request after console spam.", NotificationType.WARNING);
            return;
        }

        if (args.length > 0) {
            dev.risas.module.impl.other.Spammer.customMessage = String.join(" ", args);
            Rise.INSTANCE.getNotificationManager().registerNotification("Successfully set the spammer message.");
        } else {
            Rise.INSTANCE.getNotificationManager().registerNotification("Please input a message.");
        }
    }
}
