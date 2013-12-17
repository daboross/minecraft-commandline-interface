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
package net.daboross.mccli;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.daboross.mccli.api.MinecraftInterface;
import net.daboross.mccli.clients.CurrentlyRunningClientsMap;
import net.daboross.mccli.command.CommandHandler;
import net.daboross.mccli.command.Sender;
import net.daboross.mccli.commands.ConnectMultipleOfflineMode;
import net.daboross.mccli.commands.ConnectOfflineMode;
import net.daboross.mccli.commands.ConnectOnlineMode;
import net.daboross.mccli.commands.Disconnect;
import net.daboross.mccli.commands.EndCommand;
import net.daboross.mccli.commands.HelpCommand;
import net.daboross.mccli.commands.ListConnected;
import net.daboross.mccli.commands.LocalHostAlias;
import net.daboross.mccli.commands.ParseFileCommand;
import net.daboross.mccli.commands.SendText;
import net.daboross.mccli.input.InputHandlerThread;
import net.daboross.mccli.log.MCIO;
import net.daboross.mccli.log.SubLogger;

public class Main implements MinecraftInterface {

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    private final MCIO output;
    private final Logger logger;
    private final InputHandlerThread input;
    private final CommandHandler commands;
    private final CurrentlyRunningClientsMap clients;

    public Main() {
        output = new MCIO();
        logger = output.getLogger();
        input = output.getInputThread();
        commands = new CommandHandler(this);
        clients = new CurrentlyRunningClientsMap();
    }

    public void start() {
        logger.log(Level.INFO, "Starting version {0}", getVersion());
        commands.addCommand(new HelpCommand(commands));
        commands.addCommand(new EndCommand(this));
        commands.addCommand(new ConnectOnlineMode(this));
        commands.addCommand(new ConnectOfflineMode(this));
        commands.addCommand(new LocalHostAlias(this));
        commands.addCommand(new SendText(this));
        commands.addCommand(new Disconnect(this));
        commands.addCommand(new ListConnected(this));
        commands.addCommand(new ConnectMultipleOfflineMode(this));
        ParseFileCommand pfcmd = new ParseFileCommand(this);
        pfcmd.prepareThreads();
        commands.addCommand(pfcmd);
        commands.addCommand(pfcmd.getRunCommand());
        output.start(commands);
    }

    private String getVersion() {
        String version = Main.class.getPackage().getImplementationVersion();
        return version == null ? "mc-cli-Unknown" : version;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public InputHandlerThread getInput() {
        return input;
    }

    @Override
    public SubLogger getSubLogger(String name) {
        return new SubLogger(name, logger);
    }

    @Override
    public void dispatchCommand(Sender sender, String command, String args) {
        commands.dispatchCommand(sender, args);
    }

    @Override
    public CurrentlyRunningClientsMap getClients() {
        return clients;
    }

    @Override
    public void stop() {
        logger.log(Level.INFO, "Ending version {0}", getVersion());
        clients.endAllClients();
        output.end();
        System.exit(0);
    }
}
