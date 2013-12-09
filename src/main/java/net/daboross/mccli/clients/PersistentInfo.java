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
package net.daboross.mccli.clients;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.theunnameddude.mcclient.api.auth.AuthenticationResponse;

public class PersistentInfo {

    private final Map<String, AuthenticationResponse> online = new HashMap<>();
    private final Map<String, SavedServer> servers = new HashMap<>();

    public AuthenticationResponse getAccount(String username) {
        return online.get(username.toLowerCase());
    }

    public void addAccount(AuthenticationResponse response) {
        online.put(response.getUsername().toLowerCase(), response);
    }

    public void addServer(SavedServer server) {
        servers.put(server.getName().toLowerCase(), server);
    }

    public Collection<SavedServer> getSavedServers() {
        return Collections.unmodifiableCollection(servers.values());
    }
}
