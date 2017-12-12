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
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;

public class CurrentlyRunningClientsMap {

    private final Map<Client, String> clients = new HashMap<>();

    public void addClient(Client client, String name) {
        client.getSession().addListener(new ClientListenerRemover(client));
        clients.put(client, name);
    }

    public void endAllClients() {
        for (Iterator<Client> i = clients.keySet().iterator(); i.hasNext(); ) {
            Client next = i.next();
            i.remove();
            next.getSession().disconnect("Ending all clients");
        }
    }

    public List<Map.Entry<Client, String>> getClientsWith(String nameRegex) {
        List<Map.Entry<Client, String>> list = new ArrayList<>();
        for (Map.Entry<Client, String> entry : clients.entrySet()) {
            if (entry.getValue().matches(nameRegex)) {
                list.add(entry);
            }
        }
        return list;
    }

    public Set<Map.Entry<Client, String>> getAllClients() {
        return Collections.unmodifiableSet(clients.entrySet());
    }

    private class ClientListenerRemover extends SessionAdapter {

        private final Client client;

        public ClientListenerRemover(Client client) {
            this.client = client;
        }

        @Override
        public void disconnected(final DisconnectedEvent event) {
            clients.remove(client);
        }
    }
}
