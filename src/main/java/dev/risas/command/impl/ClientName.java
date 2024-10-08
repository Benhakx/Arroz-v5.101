package dev.risas.command.impl;

import dev.risas.Rise;
import dev.risas.command.Command;
import dev.risas.command.api.CommandInfo;
import dev.risas.util.render.theme.ThemeUtil;

@CommandInfo(name = "ClientName", description = "Sets the custom client name", syntax = ".clientname <name>", aliases = "clientname")
public final class ClientName extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        ThemeUtil.setCustomClientName(String.join(" ", args));
        Rise.INSTANCE.getNotificationManager().registerNotification("Successfully set the client name.");
    }
}
