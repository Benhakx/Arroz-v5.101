package dev.risas.command.impl;

import dev.risas.Rise;
import dev.risas.command.Command;
import dev.risas.command.api.CommandInfo;
import dev.risas.config.ConfigHandler;

@CommandInfo(name = "Config", description = "Modifies your configs", syntax = ".config <save/load/list/delete> <name>", aliases = {"config", "cfg"})
public final class Config extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        Rise.INSTANCE.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                switch (args[0].toLowerCase()) {
                    case "save": {
                        ConfigHandler.save(args[1]);
                        break;
                    }

                    case "load": {
                        ConfigHandler.load(args[1]);
                        break;
                    }

                    case "list": {
                        ConfigHandler.list();
                        break;
                    }

                    case "delete": {
                        ConfigHandler.delete(args[1]);
                        break;
                    }
                }
            }
        });
    }
}
