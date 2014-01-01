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
package net.daboross.mccli.input;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.console.ConsoleReader;
import net.daboross.mccli.command.CommandHandler;
import net.daboross.mccli.command.Sender;
import net.daboross.mccli.log.ChatColor;
import net.daboross.mccli.utils.ArrayUtils;

public class InputHandlerThread extends Thread {

    private final Logger logger;
    private final CommandHandler commandHandler;
    private final ConsoleReader console;
    private final Sender consoleSender;
    private final InputParser parser;

    public InputHandlerThread(Logger logger, CommandHandler commandHandler, ConsoleReader console) {
        super("Input Thread");
        this.logger = logger;
        this.commandHandler = commandHandler;
        this.console = console;
        this.consoleSender = new ConsoleCommandSender(logger);
        this.parser = new InputParser();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                String line = console.readLine("> ");
                if (line != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        logger.log(Level.INFO, "{0}\\n isn''t a command", ChatColor.DARK_RED);
                        continue;
                    }
                    line = parser.parse(line);
                    String[] args = line.split(" ");
                    commandHandler.dispatchCommand(consoleSender, args[0], ArrayUtils.sub(args));
                }
            } catch (Throwable t) {
                logger.log(Level.INFO, "Error in input thread", t);
            }
        }
    }

    public String passwordInput(String prompt) {
        try {
            return console.readLine(prompt, (char) 0);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error getting password", ex);
            throw new RuntimeException("Error getting password", ex);
        }
    }
}
