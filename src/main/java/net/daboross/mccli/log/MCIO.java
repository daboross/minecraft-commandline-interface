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
package net.daboross.mccli.log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import jline.internal.Log;
import net.daboross.mccli.command.CommandHandler;
import net.daboross.mccli.input.InputHandlerThread;

public class MCIO {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private ConsoleReader consoleReader;
    private ClientLogger logger;
    private InputHandlerThread inputThread;

    public MCIO() {
        File dataDir = new File(new File(new File(System.getProperty("user.home"), ".local"), "share"), "mc-cli");
        try {
            consoleReader = new ConsoleReader();
            consoleReader.setHistory(new FileHistory(new File(dataDir, "command-history.log")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        logger = new ClientLogger(consoleReader, dataDir);
    }

    public void start(CommandHandler commandHandler) {
        Log.setOutput(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
            }
        }));
        logger.startDispatcher();
        System.setErr(new PrintStream(new LoggingOutputStream(Level.SEVERE), true));
        System.setOut(new PrintStream(new LoggingOutputStream(Level.INFO), true));
        if (consoleReader.getTerminal() instanceof UnsupportedTerminal) {
            logger.info("Unable to initialize jline.");
        }
        inputThread = new InputHandlerThread(logger, commandHandler, consoleReader);
        inputThread.start();
    }

    public ClientLogger getLogger() {
        return logger;
    }

    public InputHandlerThread getInputThread() {
        return inputThread;
    }

    public void end() {
        if (inputThread != null) {
            inputThread.interrupt();
        }
        try {
            ((FileHistory) consoleReader.getHistory()).flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class LoggingOutputStream extends ByteArrayOutputStream {

        private final Level level;

        public LoggingOutputStream(Level level) {
            this.level = level;
        }

        @Override
        public void flush() throws IOException {
            String contents = toString();
            super.reset();
            if (!contents.isEmpty() && !contents.equals(LINE_SEPARATOR)) {
                logger.log(level, contents);
            }
        }
    }
}
