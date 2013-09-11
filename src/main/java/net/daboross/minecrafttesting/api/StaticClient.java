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
package net.daboross.minecrafttesting.api;

import java.util.logging.Logger;
import net.daboross.minecrafttesting.command.Sender;
import net.daboross.minecrafttesting.log.SubLogger;

/**
 *
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class StaticClient {

    private static GlobalClient client;

    public static void setClient(GlobalClient client) {
        StaticClient.client = client;
    }

    public static Logger getLogger() {
        return client == null ? null : client.getLogger();
    }

    public static SubLogger getSubLogger(String name) {
        return client == null ? null : client.getSubLogger(name);
    }

    public static void dispatchCommand(Sender sender, String command, String args) {
        if (client != null) {
            client.dispatchCommand(sender, command, args);
        }
    }

    public static GlobalClient getClient() {
        return client;
    }

    public static void stop() {
        if (client != null) {
            client.stop();
        }
    }
}
