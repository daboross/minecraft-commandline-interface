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

import java.util.logging.Level;
import net.daboross.mccli.api.MinecraftInterface;
import net.daboross.mccli.command.Command;
import net.daboross.mccli.command.Sender;
import net.daboross.mccli.connect.LoggingClientListener;
import net.daboross.mccli.log.ChatColor;
import org.spacehq.mc.auth.exception.AuthenticationException;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

public class Connect extends Command {

    private final MinecraftInterface main;

    public Connect(MinecraftInterface main) {
        super("connect");
        setHelpText("Connect to a server offline mode");
        setHelpArgs("HOST[:PORT]", "[-o]", "USERNAMES...");
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

        boolean online = args[1].equalsIgnoreCase("-o") || (args.length > 2 && args[2].equalsIgnoreCase("-o"));
        for (int i = (online ? 2 : 1); i < args.length; i++) {
            String username = args[i];
            MinecraftProtocol protocol;
            if (online) {
                String password = main.getInput().passwordInput("(pass for " + username + ")# ");
                try {
                    protocol = new MinecraftProtocol(username, password, false);
                } catch (AuthenticationException e) {
                    main.getLogger().log(Level.WARNING, "Authenticator error: {0}", e.toString());
                    continue;
                }
            } else {
                protocol = new MinecraftProtocol(username);
            }
            sender.sendMessage(ChatColor.GREEN + "Connecting to " + ChatColor.DARK_RED + host + ":" + port + ChatColor.GREEN + " with username " + ChatColor.DARK_RED + username);
            Client client = new Client(host, port, protocol, new TcpSessionFactory());
            client.getSession().addListener(new LoggingClientListener(main.getSubLogger(host + ":" + username)));
            main.getClients().addClient(client, username);
            client.getSession().connect();
        }
    }
}
