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
package net.daboross.mccli.commands;

import java.util.Map;
import net.daboross.mccli.api.MinecraftInterface;
import net.daboross.mccli.command.Command;
import net.daboross.mccli.command.Sender;
import net.daboross.mccli.log.ChatColor;
import net.daboross.mccli.utils.ArrayUtils;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Client;

public class SendText extends Command {

    private final MinecraftInterface main;

    public SendText(MinecraftInterface main) {
        super("send");
        setHelpArgs("Name regex", "message");
        setHelpText("Sends a chat message using the specified client(s). Name regex is a regex that will be matched against 'host:username'");
        this.main = main;
    }

    @Override
    public void run(Sender sender, String commandLabel, String[] args) {
        if (args.length < 2) {
            sendHelpText(sender);
            return;
        }
        String name = args[0];
        String message = ArrayUtils.join(args, 1, " ");
        for (Map.Entry<Client, String> client : main.getClients().getClientsWith(name)) {
            sender.sendMessage(ChatColor.GREEN + "Sending message to " + ChatColor.DARK_RED + client.getValue());
            client.getKey().getSession().send(new ClientChatPacket(message));
        }
    }
}
