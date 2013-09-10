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
package net.daboross.minecrafttesting.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;
import jline.internal.Log;
import net.daboross.minecrafttesting.command.CommandHandler;
import net.daboross.minecrafttesting.command.Sender;
import org.fusesource.jansi.AnsiConsole;

/**
 *
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class MCInputOutput {

    private boolean running;
    private ConsoleReader consoleReader;
    private final ConsoleSender sender;
    private MCLogger logger;
    private final CommandHandler commands;

    public MCInputOutput(CommandHandler commands) {
        AnsiConsole.systemInstall();
        try {
            consoleReader = new ConsoleReader();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        logger = new MCLogger(consoleReader);
        this.sender = new ConsoleSender();
        this.commands = commands;
    }

    public void start() {
        running = true;
        Log.setOutput(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
            }
        }));
        logger.startDispatcher();
        System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));

        if (consoleReader.getTerminal() instanceof UnsupportedTerminal) {
            logger.info("Unable to initialize fancy terminal.");
        }
        new Thread(new InputRunnable()).start();
    }

    public MCLogger getLogger() {
        return logger;
    }

    public void end() {
        running = false;
    }

    private class ConsoleSender implements Sender {

        @Override
        public void sendMessage(String message) {
            sendMessage(Level.INFO, message);
        }

        @Override
        public void sendMessage(Level level, String message) {
            logger.log(level == null ? Level.INFO : level, message);
        }
    }

    private class InputRunnable extends Thread {

        public InputRunnable() {
            super("Input Thread");
        }

        @Override
        public void run() {
            try {
                logger.info("Starting input");
                while (running && !isInterrupted()) {
                    String line = consoleReader.readLine("> ");
                    if (line != null) {
                        commands.actOn(sender, ChatColor.translateAlternateColorCodes('&', line));
                    }
                }
            } catch (IOException ex) {
            }
        }
    }
}
