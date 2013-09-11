package net.daboross.minecrafttesting;

import net.daboross.minecrafttesting.command.CommandHandler;
import net.daboross.minecrafttesting.log.MCOutput;
import java.util.logging.Logger;
import net.daboross.minecrafttesting.api.GlobalClient;
import net.daboross.minecrafttesting.api.StaticClient;
import net.daboross.minecrafttesting.command.Sender;
import net.daboross.minecrafttesting.commands.ConnectOfflineMode;
import net.daboross.minecrafttesting.commands.ConnectOnlineMode;
import net.daboross.minecrafttesting.commands.EndCommand;
import net.daboross.minecrafttesting.commands.HelpCommand;
import net.daboross.minecrafttesting.commands.LocalHostAlias;
import net.daboross.minecrafttesting.commands.SendText;
import net.daboross.minecrafttesting.log.SubLogger;

/**
 * Hello world!
 *
 */
public class Main implements GlobalClient {

    private final MCOutput output;
    private final Logger logger;
    private final CommandHandler commands;

    public Main() {
        commands = new CommandHandler();
        output = new MCOutput();
        logger = output.getLogger();
        logger.info("Starting up MinecraftTesting");
        commands.addCommand(new HelpCommand(commands));
        commands.addCommand(new EndCommand());
        commands.addCommand(new ConnectOnlineMode());
        commands.addCommand(new ConnectOfflineMode());
        commands.addCommand(new LocalHostAlias());
        commands.addCommand(new SendText());
    }

    public void start() {
        output.start(commands);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public SubLogger getSubLogger(String name) {
        return new SubLogger(name, logger);
    }

    @Override
    public void dispatchCommand(Sender sender, String command, String args) {
        commands.dispatchCommand(sender, args);
    }

    public static void main(String[] args) {
        Main main = new Main();
        StaticClient.setClient(main);
        main.start();
    }

    @Override
    public void stop() {
        logger.info("Shutting down!");
        output.end();
        System.exit(0);
    }
}
