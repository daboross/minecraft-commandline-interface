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
package net.daboross.mccli.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;
import jline.internal.Log;
import net.daboross.mccli.command.CommandHandler;
import net.daboross.mccli.input.InputHandlerThread;

/**
 *
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class MCOutput {

    private ConsoleReader consoleReader;
    private ClientLogger logger;
    private Thread inputThread;

    public MCOutput() {
        try {
            consoleReader = new ConsoleReader();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        logger = new ClientLogger(consoleReader);
    }

    public void start(CommandHandler commandHandler) {
        Log.setOutput(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
            }
        }));
        logger.startDispatcher();
        System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));
        if (consoleReader.getTerminal() instanceof UnsupportedTerminal) {
            logger.info("Unable to initialize jline.");
        }
        inputThread = new InputHandlerThread(logger, commandHandler, consoleReader);
        inputThread.start();
    }

    public ClientLogger getLogger() {
        return logger;
    }

    public void end() {
        if (inputThread != null) {
            inputThread.interrupt();
        }
    }
}
