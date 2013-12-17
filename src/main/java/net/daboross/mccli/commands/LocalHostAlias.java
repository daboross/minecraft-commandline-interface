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

import net.daboross.mccli.api.MinecraftInterface;
import net.daboross.mccli.command.Command;
import net.daboross.mccli.command.Sender;
import net.daboross.mccli.connect.LoggingClientListener;
import net.daboross.mccli.log.ChatColor;
import net.theunnameddude.mcclient.api.MinecraftClient;
import net.theunnameddude.mcclient.api.MinecraftClientConnector;
import net.theunnameddude.mcclient.api.auth.AuthenticationResponse;
import net.theunnameddude.mcclient.api.auth.Authenticator;
import net.theunnameddude.mcclient.protocol.ver1_6_4.PacketConstructor1_6_4;
import net.theunnameddude.mcclient.protocol.ver1_7_2.PacketConstructor1_7_2;

public class LocalHostAlias extends Command {

    private final MinecraftInterface main;

    public LocalHostAlias(MinecraftInterface main) {
        super("local");
        setHelpText("Connects to the offline server localhost:25565");
        setHelpArgs("-6", "Usernames...");
        this.main = main;
    }

    @Override
    public void run(Sender sender, String commandLabel, String[] args) {
        if (args.length == 0) {
            sendHelpText(sender);
            return;
        }
        String host = "localhost";
        int port = 25565;
        boolean use1_6_4 = args[0].equals("-6");
        for (int i = use1_6_4 ? 1 : 0; i < args.length; i++) {
            String username = args[i];
            sender.sendMessage(ChatColor.GREEN + "Connecting to " + ChatColor.DARK_RED + host + ":" + port + ChatColor.GREEN + " with username " + ChatColor.DARK_RED + username);
            AuthenticationResponse auth = Authenticator.offlineMode(username);
            MinecraftClient client = MinecraftClientConnector.connect(host, port, auth, use1_6_4 ? new PacketConstructor1_6_4() : new PacketConstructor1_7_2());
            client.addListener(new LoggingClientListener(main.getSubLogger(host + ":" + username)));
            main.getClients().addClient(client, host + ":" + username);
        }
    }
}
