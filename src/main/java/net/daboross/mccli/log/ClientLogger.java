package net.daboross.mccli.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import jline.console.ConsoleReader;

public class ClientLogger extends Logger {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private final ColouredWriter writer;
    private final LogDispatcher dispatcher = new LogDispatcher(this);
    private final Formatter formatter = new ConciseFormatter();

    public ClientLogger(ConsoleReader consoleReader, File dataDir) {
        super("Client", null);
        this.writer = new ColouredWriter(consoleReader);
        try {
            FileHandler handler = new FileHandler(new File(dataDir, "minecraft-client.log").getAbsolutePath(), 1 << 24, 8, true);
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

    public class LogDispatcher extends Thread {

        private final ClientLogger logger;
        private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

        public LogDispatcher(ClientLogger logger) {
            super("Logging Thread");
            this.logger = logger;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                LogRecord record;
                try {
                    record = queue.take();
                } catch (InterruptedException ex) {
                    continue;
                }
                logger.doLog(record);
            }
            for (LogRecord record : queue) {
                logger.doLog(record);
            }
        }

        public void queue(LogRecord record) {
            if (!isInterrupted()) {
                queue.add(record);
            }
        }
    }

    public class ConciseFormatter extends Formatter {

        @Override
        public String format(LogRecord record) {
            StringBuilder formatted = new StringBuilder();
            formatted.append(DATE_FORMAT.format(record.getMillis()));
            formatted.append(" [").append(record.getLevel().getLocalizedName()).append("] ");
            formatted.append(formatMessage(record));
            formatted.append('\n');
            if (record.getThrown() != null) {
                StringWriter writer = new StringWriter();
                try (PrintWriter printWriter = new PrintWriter(writer)) {
                    record.getThrown().printStackTrace(printWriter);
                }
                formatted.append(writer);
            }
            return formatted.toString();
        }
    }
}