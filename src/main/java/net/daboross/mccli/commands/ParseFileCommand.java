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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
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

public class ParseFileCommand extends Command {

    private final MinecraftInterface main;
    private List<MCLoginAccount> accounts;

    public ParseFileCommand(MinecraftInterface main) {
        super("parse");
        setHelpText("Connect to a server");
        setHelpArgs("Host:Port", "-6");
        this.main = main;
    }

    public Command getRunCommand() {
        return new RunParseFile();
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
        boolean use1_6_4 = args[1].equals("-6");
        File f = new File(new File(System.getProperty("user.home")), "MCAccounts.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(f.toPath(), Charset.forName("UTF-8"));
        } catch (IOException ex) {
            main.getLogger().log(Level.SEVERE, "Failed to read all lines.", ex);
            return;
        }
        for (int i = 0; i < lines.size(); i++) {
            String username = args[i];
            String password = main.getInput().passwordInput("(pass for " + username + ")# ");
            sender.sendMessage(ChatColor.GREEN + "Connecting to " + ChatColor.DARK_RED + host + ":" + port + ChatColor.GREEN + " with username " + ChatColor.DARK_RED + username);
            AuthenticationResponse auth;
            try {
                auth = Authenticator.sendRequest(username, password);
            } catch (Exception e) {
                main.getLogger().log(Level.WARNING, "Authenticator error: {0}", e.toString());
                return;
            }
            MinecraftClient client = MinecraftClientConnector.connect(host, port, auth, use1_6_4 ? new PacketConstructor1_6_4() : new PacketConstructor1_7_2());
            client.addListener(new LoggingClientListener(main.getSubLogger(host + ":" + username)));
            main.getClients().addClient(client, username);
        }
    }

    private class RunParseFile extends Command {

        public RunParseFile() {
            super("run-parsed");
        }

        @Override
        public void run(Sender sender, String commandLabel, String[] args) {
            if (args.length < 2) {
                sendHelpText(sender);
                return;
            }
            String[] hostport = args[0].split(":");
            String host = hostport[0];
            int port;
            try {
                port = hostport.length > 1 ? Integer.parseInt(hostport[1]) : 25565;
            } catch (NumberFormatException ex) {
                sender.sendMessage(Level.WARNING, ChatColor.GREEN.toString() + hostport[1] + ChatColor.DARK_RED + " isn't an integer");
                return;
            }
            boolean use1_6_4 = args[1].equals("-6");
            if (accounts == null) {
                sender.sendMessage("No accounts loaded.");
                return;
            }
            for (MCLoginAccount account : accounts) {
                String username = account.username;
                String password = account.password;
                sender.sendMessage(ChatColor.GREEN + "Connecting to " + ChatColor.DARK_RED + host + ":" + port + ChatColor.GREEN + " with username " + ChatColor.DARK_RED + username);
                AuthenticationResponse auth;
                try {
                    auth = Authenticator.sendRequest(username, password);
                } catch (Exception e) {
                    main.getLogger().log(Level.WARNING, "Authenticator error with user (" + username + "): {0}", e.toString());
                    return;
                }
                MinecraftClient client = MinecraftClientConnector.connect(host, port, auth, use1_6_4 ? new PacketConstructor1_6_4() : new PacketConstructor1_7_2());
                client.addListener(new LoggingClientListener(main.getSubLogger(host + ":" + username)));
                main.getClients().addClient(client, username);
            }
        }
    }

    private class MCLoginAccount {

        private String id;
        private String username;
        private String password;
    }
}
