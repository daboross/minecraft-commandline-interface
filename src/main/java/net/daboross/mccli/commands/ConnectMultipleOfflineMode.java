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

/**
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class ConnectMultipleOfflineMode extends Command {

    private final MinecraftInterface main;

    public ConnectMultipleOfflineMode(MinecraftInterface main) {
        super("com");
        setHelpText("Connect to a server offline mode multiple times");
        setHelpArgs("Host:Port", "Usernames");
        this.main = main;
    }

    @Override
    public void run(Sender sender, String commandLabel, String[] args) {
        if (args.length < 3) {
            sendHelpText(sender);
            return;
        }
        String[] hostport = args[0].split(":", 2);
        String host = hostport[0];
        String port = hostport.length > 1 ? hostport[1] : "25565";
        int portInt;
        try {
            portInt = Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            sender.sendMessage(Level.WARNING, ChatColor.GREEN + port + ChatColor.DARK_RED + " isn't an integer");
            return;
        }
        for (int i = 2; i < args.length; i++) {
            String username = args[i];
            sender.sendMessage(ChatColor.GREEN + "Connecting to " + ChatColor.DARK_RED + host + ":" + portInt + ChatColor.GREEN + " with username " + ChatColor.DARK_RED + username);
            AuthenticationResponse auth = Authenticator.offlineMode(username);
            MinecraftClient client = MinecraftClientConnector.connect(host, portInt, auth);
            client.addListener(new LoggingClientListener(main.getSubLogger(host + ":" + username)));
            main.getClients().addClient(client, username);
        }
    }
}
