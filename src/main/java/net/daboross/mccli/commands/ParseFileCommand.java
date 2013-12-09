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
package net.daboross.mccli.commands;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import net.daboross.mccli.api.MinecraftInterface;
import net.daboross.mccli.command.Command;
import net.daboross.mccli.command.Sender;
import net.daboross.mccli.connect.LoggingClientListener;
import net.daboross.mccli.log.ChatColor;
import net.theunnameddude.mcclient.api.MinecraftClient;
import net.theunnameddude.mcclient.api.auth.AuthenticationResponse;
import net.theunnameddude.mcclient.client.MinecraftClientImpl;
import net.theunnameddude.mcclient.protocol.ver1_6_4.PacketConstructor1_6_4;
import net.theunnameddude.mcclient.protocol.ver1_7_2.PacketConstructor1_7_2;
import org.json.JSONStringer;
import org.json.JSONWriter;

public class ParseFileCommand extends Command {

    private final MinecraftInterface main;
    private List<MCLoginAccount> accounts;
    private final Deque<Runnable> nextToRun = new LinkedList<Runnable>();

    public ParseFileCommand(MinecraftInterface main) {
        super("parse");
        setHelpText("Parses the file.");
        this.main = main;
    }

    public void prepareThreads() {
        for (int i = 0; i < 25; i++) {
            Thread thread = new Thread(new RunningRunnable(), "RunningRunnable " + 1);
            thread.start();
        }
    }

    public Command getRunCommand() {
        return new RunParseFile();
    }

    @Override
    public void run(final Sender sender, String commandLabel, String[] args) {
        if (args.length != 0) {
            sendHelpText(sender);
            return;
        }
        File f = new File(new File(System.getProperty("user.home")), "MCAccounts.txt");
        sender.sendMessage("Reading from " + f.getAbsolutePath());
        List<String> lines;
        try {
            lines = Files.readAllLines(f.toPath(), Charset.forName("UTF-8"));
        } catch (IOException ex) {
            main.getLogger().log(Level.SEVERE, "Failed to read all lines.", ex);
            return;
        }
        accounts = Collections.synchronizedList(new ArrayList<MCLoginAccount>(lines.size() / 3));
        sender.sendMessage("lines.size = " + lines.size());
        int state = 0; // 0 = ID; 1 = username; 2 = pass; -1 = waiting.
        MCLoginAccount next = new MCLoginAccount();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                state = -1;
            } else if (state == -1) {
                if (i > lines.size() - 3) {
                    break;
                }
                if (line.equals("id") && lines.get(i + 1).trim().equals("username")
                        && lines.get(i + 2).trim().equals("password")) {
                    state = 0;
                    i += 2;
                }
            } else if (state == 0) {
                next.id = line;
                state = 1;
            } else if (state == 1) {
                next.username = line;
                state = 2;
            } else if (state == 2) {
                next.password = line;
                final MCLoginAccount thisNext = next;
//                Runnable auth = new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            thisNext.auth = Authenticator.sendRequest(thisNext.username, thisNext.password);
//                        } catch (RuntimeException | IOException ignored) {
//                            sender.sendMessage(String.format("INVALID: id: %s, user: %s, pass: %s", thisNext.id, thisNext.username, thisNext.password));
//                            return;
//                        }
                sender.sendMessage(String.format("id: %s, user: %s, pass: %s", thisNext.id, thisNext.username, thisNext.password));
                accounts.add(thisNext);
//                    }
//                };
//                synchronized (nextToRun) {
//                    nextToRun.add(auth);
//                    nextToRun.notify();
//                }
                next = new MCLoginAccount();
                state = 0;
            }
        }
        sender.sendMessage("Done");
    }

    public Command getSaveCommand() {
        return new SaveParsed();
    }

    private class RunParseFile extends Command {

        public RunParseFile() {
            super("run-parsed");
            setHelpText("Connect to a server with all parsed accounts");
            setHelpArgs("Host:Port", "-6");
        }

        @Override
        public void run(final Sender sender, String commandLabel, String[] args) {
            if (args.length < 1 || args.length > 2) {
                sendHelpText(sender);
                return;
            }
            String[] hostport = args[0].split(":");
            final String host = hostport[0];
            final int port;
            try {
                port = hostport.length > 1 ? Integer.parseInt(hostport[1]) : 25565;
            } catch (NumberFormatException ex) {
                sender.sendMessage(Level.WARNING, ChatColor.GREEN.toString() + hostport[1] + ChatColor.DARK_RED + " isn't an integer");
                return;
            }
            final boolean use1_6_4 = args.length == 2 && args[1].equals("-6");
            if (accounts == null) {
                sender.sendMessage("No accounts loaded.");
                return;
            }
            for (final MCLoginAccount account : accounts) {
                final MinecraftClient client = new MinecraftClientImpl();
                client.addListener(new LoggingClientListener(main.getSubLogger(host + ":" + account.username)));
                main.getClients().addClient(client, account.username);
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        sender.sendMessage(ChatColor.GREEN + "Connecting to " + ChatColor.DARK_RED + host + ":" + port + ChatColor.GREEN + " with username " + ChatColor.DARK_RED + account.username);
                        client.connect(host, port, account.auth, use1_6_4 ? new PacketConstructor1_6_4() : new PacketConstructor1_7_2());
                    }
                };
                synchronized (nextToRun) {
                    nextToRun.add(r);
                    nextToRun.notify();
                }
            }
        }
    }

    private class SaveParsed extends Command {

        public SaveParsed() {
            super("save-parsed");
            setHelpText("Save");
        }

        @Override
        public void run(final Sender sender, String commandLabel, String[] args) {
            if (args.length != 0) {
                sendHelpText(sender);
                return;
            }
            JSONWriter w = new JSONStringer().object().key("accounts").array();
            for (final MCLoginAccount account : accounts) {
                w.object()
                        .key("username").value(account.username)
                        .key("password").value(account.password)
                        .endObject();
            }
            sender.sendMessage(w.endArray().endObject().toString());
            List<CharSequence> l = new ArrayList<>();
            l.add(w.toString());
            try {
                Files.write(new File(new File(System.getProperty("user.home")), "save-accounts.json").toPath(), l, Charset.forName("UTF-8"));
            } catch (IOException ex) {
                main.getLogger().log(Level.SEVERE, null, ex);
            }
        }
    }

    private class MCLoginAccount {

        private String id;
        private String username;
        private String password;
        private AuthenticationResponse auth;
    }

    private class RunningRunnable implements Runnable {

        @Override
        public void run() {
            while (true) {
                Runnable next;
                synchronized (nextToRun) {
                    next = nextToRun.poll();
                    if (next == null) {
                        main.getLogger().log(Level.INFO, "Runnable out of runnables.");
                        try {
                            nextToRun.wait();
                        } catch (InterruptedException ex) {
                            main.getLogger().log(Level.WARNING, "InterruptedException in RunningRunnable.", ex);
                        }
                    }
                }
                if (next != null) {
                    next.run();
                }
            }
        }
    }
}
