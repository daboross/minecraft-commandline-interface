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
import org.spacehq.packetlib.Client;

public class Disconnect extends Command {

    private final MinecraftInterface main;

    public Disconnect(MinecraftInterface main) {
        super("disconnect");
        setHelpArgs("Name regex");
        setHelpText("Disconnects the given client(s). Name regex is a regex that will be matched against 'host:username'");
        this.main = main;
    }

    @Override
    public void run(Sender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            sendHelpText(sender);
            return;
        }
        String name = args[0];
        for (Map.Entry<Client, String> entry : main.getClients().getClientsWith(name)) {
            sender.sendMessage(ChatColor.GREEN + "Disconnecting " + ChatColor.DARK_RED + entry.getValue());
            final Client client = entry.getKey();
            main.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    client.getSession().disconnect("Shutting down");
                }
            });
        }
    }
}
