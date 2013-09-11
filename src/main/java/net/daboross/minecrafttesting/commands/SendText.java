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
package net.daboross.minecrafttesting.commands;

import java.util.List;
import java.util.Map;
import net.daboross.minecrafttesting.clients.CurrentlyRunningClientsMap;
import net.daboross.minecrafttesting.command.Command;
import net.daboross.minecrafttesting.command.Sender;
import net.daboross.minecrafttesting.log.ChatColor;
import net.daboross.minecrafttesting.utils.ArrayUtils;
import net.theunnameddude.mcclient.api.MinecraftClient;

/**
 *
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class SendText extends Command {

    public SendText() {
        super("send");
        setHelpArgs("Name regex", "message");
        setHelpText("Sends a chat message using the specified client(s). Name regex is a regex that will be matched against 'host:port:username'");
    }

    @Override
    public void run(Sender sender, String commandLabel, String[] args) {
        if (args.length < 2) {
            sendHelpText(sender);
            return;
        }
        String name = args[0];
        String message = ArrayUtils.join(args, 1, " ");
        for (Map.Entry<MinecraftClient, String> client : CurrentlyRunningClientsMap.getInstance().getClientsWith(name)) {
            sender.sendMessage(ChatColor.GREEN + "Sending message to " + client.getValue());
            client.getKey().sendMessage(message);
        }
    }
}
