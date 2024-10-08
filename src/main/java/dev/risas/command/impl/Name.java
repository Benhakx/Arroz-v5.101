package dev.risas.command.impl;

import dev.risas.Rise;
import dev.risas.command.Command;
import dev.risas.command.api.CommandInfo;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

@CommandInfo(name = "Name", description = "Copies your username to the clipboard", syntax = ".name", aliases = "name")
public final class Name extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        final StringSelection stringSelection = new StringSelection(mc.thePlayer.getName());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
        Rise.INSTANCE.getNotificationManager().registerNotification("Copied your username to the clipboard.");
    }
}
