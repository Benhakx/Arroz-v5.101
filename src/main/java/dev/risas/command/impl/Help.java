package dev.risas.command.impl;

import dev.risas.Rise;
import dev.risas.command.Command;
import dev.risas.command.CommandManager;
import dev.risas.command.api.CommandInfo;
import net.minecraft.util.EnumChatFormatting;

@CommandInfo(name = "Help", description = "Sends all of the commands that currently exists in chat", syntax = ".help", aliases = "help")
public final class Help extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        Rise.addChatMessage(EnumChatFormatting.WHITE + "All available commands:");

        for (final Command cmd : CommandManager.COMMANDS) {
            final String description = cmd.getCommandInfo().description();
            final String alias = cmd.getCommandInfo().aliases()[0];

            if (!alias.contains("help")) {
                Rise.addChatMessage(alias + ": " + description);
            }
        }
    }
}
