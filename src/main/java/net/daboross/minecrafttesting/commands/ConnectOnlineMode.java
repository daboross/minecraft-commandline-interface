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

import java.util.logging.Level;
import net.daboross.minecrafttesting.api.StaticClient;
import net.daboross.minecrafttesting.command.Command;
import net.daboross.minecrafttesting.command.Sender;
import net.daboross.minecrafttesting.connect.LoggingClientListener;
import net.daboross.minecrafttesting.log.ChatColor;
import net.theunnameddude.mcclient.api.MinecraftClient;
import net.theunnameddude.mcclient.api.MinecraftClientConnector;
import net.theunnameddude.mcclient.api.auth.AuthenticationResponse;
import net.theunnameddude.mcclient.api.auth.Authenticator;

/**
 *
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class ConnectOnlineMode extends Command {

    public ConnectOnlineMode() {
        super("connect-online");
        setHelpText("Connect to a server");
        setHelpArgs("Host", "Port", "Username", "Password");
    }

    @Override
    public void run(Sender sender, String commandLabel, String[] args) {
        if (args.length < 4) {
            sendHelpText(sender);
            return;
        }
        String host = args[0];
        String port = args[1];
        int portInt;
        try {
            portInt = Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            sender.sendMessage(Level.WARNING, ChatColor.GREEN + port + ChatColor.DARK_RED + " isn't an integer");
            return;
        }
        String username = args[2];
        String password = args[3];
        AuthenticationResponse auth;
        try {
            auth = Authenticator.sendRequest(username, password);
        } catch (Exception e) {
            StaticClient.getLogger().log(Level.WARNING, "Authenticator error: {0}", e.toString());return;
        }
        MinecraftClient client = MinecraftClientConnector.connect(host, portInt, auth);
        client.addListener(new LoggingClientListener(StaticClient.getSubLogger(host + ":" + port).getSubLogger(username)));
    }
}
