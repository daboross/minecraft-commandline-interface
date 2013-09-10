package net.daboross.minecrafttesting.log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import jline.console.ConsoleReader;

public class MCLogger extends Logger {

    private final ColouredWriter writer;
    private final Formatter formatter = new ConciseFormatter();
    private final LogDispatcher dispatcher = new LogDispatcher(this);

    public MCLogger(ConsoleReader consoleReader) {
        super("MinecraftClient", null);
        this.writer = new ColouredWriter(consoleReader);

        try {
            FileHandler handler = new FileHandler(System.getProperty("user.home") + File.separatorChar + ".minecraft.log", 1 << 24, 8, true);
            handler.setFormatter(formatter);
            addHandler(handler);
        } catch (IOException ex) {
            System.err.println("Could not register logger!");
            ex.printStackTrace();
        }
    }

    public void startDispatcher() {
        dispatcher.start();
    }

    @Override
    public void log(LogRecord record) {
        dispatcher.queue(record);
    }

    void doLog(LogRecord record) {
        super.log(record);
        writer.print(formatter.format(record));
    }
}