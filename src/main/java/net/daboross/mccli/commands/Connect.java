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

import java.util.logging.Level;
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

public class Connect extends Command {

    private final MinecraftInterface main;

    public Connect(MinecraftInterface main) {
        super("connect");
        setHelpText("Connect to a server offline mode");
        setHelpArgs("Host:Port", "-o", "-6", "Usernames...");
        this.main = main;
    }

    @Override
    public void run(Sender sender, String commandLabel, String[] args) {
        if (args.length < 2) {
            sendHelpText(sender);
            return;
        }
        String[] hostport = args[0].split(":");
        String host = hostport[0];
        int port = hostport.length > 1 ? Integer.parseInt(hostport[1]) : 25565;
        try {
            port = hostport.length > 1 ? Integer.parseInt(hostport[1]) : 25565;
        } catch (NumberFormatException ex) {
            sender.sendMessage(Level.WARNING, ChatColor.GREEN.toString() + port + ChatColor.DARK_RED + " isn't an integer");
            return;
        }

        boolean use1_6_4 = args[1].equals("-6") || (args.length > 2 && args[2].equals("-6"));
        boolean online = args[1].equalsIgnoreCase("-o") || (args.length > 2 && args[2].equalsIgnoreCase("-o"));
        for (int i = use1_6_4 ? (online ? 3 : 2) : (online ? 2 : 1); i < args.length; i++) {
            String username = args[i];
            AuthenticationResponse auth;
            if (online) {
                String password = main.getInput().passwordInput("(pass for " + username + ")# ");
                try {
                    auth = Authenticator.sendRequest(username, password);
                } catch (Exception e) {
                    main.getLogger().log(Level.WARNING, "Authenticator error: {0}", e.toString());
                    continue;
                }
            } else {
                auth = Authenticator.offlineMode(username);
            }
            sender.sendMessage(ChatColor.GREEN + "Connecting to " + ChatColor.DARK_RED + host + ":" + port + ChatColor.GREEN + " with username " + ChatColor.DARK_RED + username);
            MinecraftClient client = MinecraftClientConnector.connect(host, port, auth, use1_6_4 ? new PacketConstructor1_6_4() : new PacketConstructor1_7_2());
            client.addListener(new LoggingClientListener(main.getSubLogger(host + ":" + username)));
            main.getClients().addClient(client, username);
        }
    }
}
