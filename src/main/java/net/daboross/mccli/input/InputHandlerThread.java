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
package net.daboross.mccli.input;

import java.util.logging.Level;
import java.util.logging.Logger;
import jline.console.ConsoleReader;
import net.daboross.mccli.command.CommandHandler;
import net.daboross.mccli.command.Sender;
import net.daboross.mccli.log.ChatColor;
import net.daboross.mccli.utils.ArrayUtils;

/**
 *
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class InputHandlerThread extends Thread {

    private final Logger logger;
    private final CommandHandler commandHandler;
    private final ConsoleReader console;
    private final Sender consoleSender;

    public InputHandlerThread(Logger logger, CommandHandler commandHandler, ConsoleReader console) {
        super("Input Thread");
        this.logger = logger;
        this.commandHandler = commandHandler;
        this.console = console;
        this.consoleSender = new ConsoleCommandSender(logger);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                String line = console.readLine("> ");
                if (line != null) {
                    if (line.trim().isEmpty()) {
                        logger.log(Level.INFO, "{0}\\n isn''t a command", ChatColor.DARK_RED);
                        continue;
                    }
                    line = ChatColor.translateAlternateColorCodes('&', line);
                    String[] args = line.split(" ");
                    commandHandler.dispatchCommand(consoleSender, args[0], ArrayUtils.sub(args));
                }
            } catch (Throwable t) {
                logger.log(Level.INFO, "Error in input thread", t);
            }
        }
    }
}
