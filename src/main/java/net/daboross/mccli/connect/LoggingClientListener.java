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
package net.daboross.mccli.connect;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.theunnameddude.mcclient.api.ClientListener;
import net.theunnameddude.mcclient.api.ProtocolStatus;
import net.theunnameddude.mcclient.client.ServerInfo;
import net.theunnameddude.mcclient.protocol.base.PacketPluginMessageBase;
import net.theunnameddude.mcclient.protocol.base.PacketRespawnBase;
import net.theunnameddude.mcclient.protocol.base.PacketTeamBase;
import org.json.JSONException;
import org.json.JSONObject;

public class LoggingClientListener extends ClientListener {

    private final Logger logger;

    public LoggingClientListener(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void onDisconnect() {
        logger.log(Level.INFO, "Disconnecting");
    }

    @Override
    public void onDisconnected() {
        logger.log(Level.INFO, "Disconnected");
    }

    @Override
    public void onAuthComplete() {
        logger.log(Level.INFO, "Authentification complete");
    }

    @Override
    public void onKick(String reason) {
        logger.log(Level.INFO, "Kicked for {0}", reason);
    }

    @Override
    public void onTeamPacket(PacketTeamBase packet) {
        logger.log(Level.INFO, "Team packet {0}", packet);
    }

    @Override
    public void onChat(JSONObject message) {
        try {
            logger.log(Level.INFO, "[Chat] {0}", message.get("text"));
        } catch (JSONException ex) {
            logger.log(Level.SEVERE, "Failed to get chat text", ex);
        }
    }

    @Override
    public void onPluginMessage(PacketPluginMessageBase packet) {
        logger.log(Level.INFO, "Plugin message recieved channel={0}, message=", new Object[]{packet.getChannel(), packet.getContent()});
    }

    @Override
    public void onRespawn(PacketRespawnBase packet) {
        logger.log(Level.INFO, "Respawning; gamemode={0}, difficulty={1}", new Object[]{packet.getGamemode(), packet.getDifficulty()});
    }

    @Override
    public void onConnected() {
        logger.log(Level.INFO, "Connected!");
    }

    @Override
    public void onAuthFail(String response) {
        logger.log(Level.INFO, "Authentification failed: {0}", response);
    }

    @Override
    public void onServerInfo(ServerInfo info) {
        logger.log(Level.INFO, "ServerInfo; difficulty={0}, LevelType={1}, GameMode={2}, maxPlayers={3}", new Object[]{info.getDifficulty(), info.getLevelType(), info.getGameMode(), info.getMaxPlayers()});
    }

    @Override
    public void onStatusChange(ProtocolStatus status) {
        logger.log(Level.INFO, "ProtocolStatus={0}", status.name());
    }
}
