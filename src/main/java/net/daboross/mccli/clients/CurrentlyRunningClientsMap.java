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
package net.daboross.mccli.clients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.theunnameddude.mcclient.api.ClientListener;
import net.theunnameddude.mcclient.api.MinecraftClient;

public class CurrentlyRunningClientsMap {

    private final Map<MinecraftClient, String> clients = new HashMap<>();

    public void addClient(MinecraftClient client, String name) {
        client.addListener(new ClientListenerRemover(client));
        clients.put(client, name);
    }

    public void endAllClients() {
        for (Iterator<MinecraftClient> i = clients.keySet().iterator(); i.hasNext(); ) {
            MinecraftClient next = i.next();
            i.remove();
            next.shutdown();
        }
    }

    public List<Map.Entry<MinecraftClient, String>> getClientsWith(String nameRegex) {
        List<Map.Entry<MinecraftClient, String>> list = new ArrayList<>();
        for (Map.Entry<MinecraftClient, String> entry : clients.entrySet()) {
            if (entry.getValue().matches(nameRegex)) {
                list.add(entry);
            }
        }
        return list;
    }

    public Set<Map.Entry<MinecraftClient, String>> getAllClients() {
        return Collections.unmodifiableSet(clients.entrySet());
    }

    private class ClientListenerRemover extends ClientListener {

        private final MinecraftClient client;

        public ClientListenerRemover(MinecraftClient client) {
            this.client = client;
        }

        @Override
        public void onDisconnected() {
            clients.remove(client);
        }
    }
}
