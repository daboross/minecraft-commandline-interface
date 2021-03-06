/*
 * Copyright (C) 2013-2014 Dabo Ross <http://www.daboross.net/>
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
package net.daboross.mccli.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.daboross.mccli.api.MinecraftInterface;
import net.daboross.mccli.log.ChatColor;

public class CommandHandler {

    private final Map<String, Command> aliasMap = new HashMap<>();
    private final List<Command> commands = new ArrayList<>();
    private final MinecraftInterface main;

    public CommandHandler(MinecraftInterface main) {
        this.main = main;
    }

    public void addCommand(Command command) {
        for (String alias : command.aliases) {
            aliasMap.put(alias, command);
        }
        commands.add(command);
    }

    public void dispatchCommand(Sender sender, String label, String... args) {
        try {
            Command command = aliasMap.get(label);
            if (command != null) {
                command.run(sender, label, args);
            } else {
                sender.sendMessage(Level.WARNING, ChatColor.DARK_RED + "Command " + ChatColor.GREEN + label + ChatColor.DARK_RED + " not found.");
            }
        } catch (Throwable t) {
            main.getLogger().log(Level.WARNING, "Failed to execute command " + label + "", t);
        }
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public Command getCommand(String alias) {
        return aliasMap.get(alias);
    }
}
