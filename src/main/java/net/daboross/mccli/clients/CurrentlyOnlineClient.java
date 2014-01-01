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

import net.theunnameddude.mcclient.api.MinecraftClient;
import net.theunnameddude.mcclient.api.auth.AuthenticationResponse;
import net.theunnameddude.mcclient.client.MinecraftClientImpl;
import net.theunnameddude.mcclient.protocol.ver1_6_4.PacketConstructor1_6_4;
import net.theunnameddude.mcclient.protocol.ver1_7_2.PacketConstructor1_7_2;

public class CurrentlyOnlineClient {

    private final String username;
    private final String host;
    private final int port;
    private final AuthenticationResponse auth;
    private final MinecraftClient client;

    public CurrentlyOnlineClient(String username, String host, int port, AuthenticationResponse auth, boolean use1_7_2) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.client = new MinecraftClientImpl();
        client.connect(host, port, auth, use1_7_2 ? new PacketConstructor1_7_2() : new PacketConstructor1_6_4());
    }

    public String getUsername() {
        return username;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public AuthenticationResponse getAuth() {
        return auth;
    }

    public MinecraftClient getClient() {
        return client;
    }
}
