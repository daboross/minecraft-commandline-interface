/*
 * Copyright (C) 2013 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.minecrafttesting.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.daboross.minecrafttesting.log.ChatColor;
import net.daboross.minecrafttesting.utils.ArrayUtils;

/**
 *
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class CommandHandler {

    private final Map<String, Command> aliasMap = new HashMap<>();
    private final List<Command> commands = new ArrayList<>();

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public CommandHandler() {
        addCommand(new HelpCommand());
    }

    public void addCommand(Command command) {
        for (String alias : command.aliases) {
            aliasMap.put(alias, command);
        }
        commands.add(command);
    }

    public void actOn(Sender sender, String string) {
        String[] commandAndArgs = string.split(" ");
        String alias = commandAndArgs[0];
        Command command = aliasMap.get(alias);
        if (command != null) {
            String[] commandArgs = ArrayUtils.sub(commandAndArgs);
            command.run(sender, alias, commandArgs);
        } else {
            sender.sendMessage(Level.WARNING, ChatColor.DARK_RED + "Command " + ChatColor.GREEN + alias + ChatColor.DARK_RED + " not found.");
        }
    }

    public class HelpCommand extends Command {

        public HelpCommand() {
            super("?", "help");
            setHelpText("Displays this help");
        }

        @Override
        public void run(Sender sender, String commandLabel, String[] args) {
            if (args.length > 0) {
                String argsString = ArrayUtils.join(args);
                for (Command command : commands) {
                    if (ArrayUtils.join(command.aliases).contains(argsString)) {
                        command.sendHelpText(sender);
                    }
                }
            } else {
                for (Command command : commands) {
                    command.sendHelpText(sender);
                }
            }
        }

    }
}
