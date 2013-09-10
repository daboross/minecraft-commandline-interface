package net.daboross.minecrafttesting;

import net.daboross.minecrafttesting.command.CommandHandler;
import net.daboross.minecrafttesting.log.MCInputOutput;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class MinecraftTestingMain {

    private final MCInputOutput io;
    private final Logger logger;
    private final CommandHandler commands;

    public MinecraftTestingMain() {
        commands = new CommandHandler();
        io = new MCInputOutput(commands);
        logger = io.getLogger();
        logger.info("Starting up MinecraftTesting");
    }

    public void start() {
        io.start();
    }

    public static void main(String[] args) {
        MinecraftTestingMain app = new MinecraftTestingMain();
        app.start();
    }
}
