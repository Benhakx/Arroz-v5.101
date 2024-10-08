package dev.risas.command.impl;

import dev.risas.Rise;
import dev.risas.command.Command;
import dev.risas.command.api.CommandInfo;

@CommandInfo(name = "PlayerHacks", description = "Sets the custom PlayerHacks target", syntax = ".playerhacks <name>", aliases = "playerhacks")
public final class PlayerHacks extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        dev.risas.module.impl.other.PlayerHacks.master = String.join(" ", args);
        Rise.INSTANCE.getNotificationManager().registerNotification("Successfully set the PlayerHacks target.");
    }
}
