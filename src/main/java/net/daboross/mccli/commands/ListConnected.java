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
package net.daboross.mccli.commands;

import java.util.Iterator;
import java.util.Map;
import net.daboross.mccli.api.MinecraftInterface;
import net.daboross.mccli.command.Command;
import net.daboross.mccli.command.Sender;
import net.daboross.mccli.log.ChatColor;
import net.theunnameddude.mcclient.api.MinecraftClient;

public class ListConnected extends Command {

    private final MinecraftInterface main;

    public ListConnected(MinecraftInterface main) {
        super("list");
        setHelpArgs();
        setHelpText("Lists all connected clients");
        this.main = main;
    }

    @Override
    public void run(Sender sender, String commandLabel, String[] args) {
        if (args.length > 0) {
            sendHelpText(sender);
            return;
        }
        Iterator<Map.Entry<MinecraftClient, String>> i = main.getClients().getAllClients().iterator();
        if (i.hasNext()) {
            StringBuilder builder = new StringBuilder(ChatColor.GREEN.toString()).append("Connected clients: ").append(ChatColor.DARK_RED).append(i.next().getValue());
            for (; i.hasNext();) {
                builder.append(ChatColor.GREEN).append(", ").append(ChatColor.DARK_RED).append(i.next().getValue());
            }
            sender.sendMessage(builder.toString());
        } else {
            sender.sendMessage(ChatColor.GREEN + "Connected clients:");
        }
    }
}
