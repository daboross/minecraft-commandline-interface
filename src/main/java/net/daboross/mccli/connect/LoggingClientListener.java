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
package net.daboross.mccli.connect;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.daboross.mccli.log.ChatColor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.spacehq.packetlib.event.session.ConnectedEvent;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.DisconnectingEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.PacketSentEvent;
import org.spacehq.packetlib.event.session.SessionListener;

public class LoggingClientListener implements SessionListener {

    private final Logger logger;

    public LoggingClientListener(Logger logger) {
        this.logger = logger;
    }
//
//    @Override
//    public void onAuthComplete() {
//        logger.log(Level.INFO, "Authentification complete");
//    }
//
//    @Override
//    public void onKick(String reason) {
//        logger.log(Level.INFO, "Kicked for {0}", parseJsonOpt(reason));
//    }
//
//    @Override
//    public void onTeamPacket(PacketTeamBase packet) {
//        logger.log(Level.INFO, "Team packet {0}", packet);
//    }
//
//    @Override
//    public void onChat(JSONObject message) {
//        try {
//            logger.log(Level.INFO, "[Chat] {0}", parseJsonMessage(message));
//        } catch (JSONException ex) {
//            logger.log(Level.SEVERE, "Failed to get chat text", ex);
//        }
//    }
//
//    @Override
//    public void onPluginMessage(PacketPluginMessageBase packet) {
//        logger.log(Level.INFO, "Plugin message recieved channel={0}, message=", new Object[]{packet.getChannel(), packet.getContent()});
//    }
//
//    @Override
//    public void onRespawn(PacketRespawnBase packet) {
//        logger.log(Level.INFO, "Respawning; gamemode={0}, difficulty={1}", new Object[]{packet.getGamemode(), packet.getDifficulty()});
//    }
//
//    @Override
//    public void onAuthFail(String response) {
//        logger.log(Level.INFO, "Authentification failed: {0}", response);
//    }
//
//    @Override
//    public void onServerInfo(ServerInfo info) {
//        logger.log(Level.INFO, "ServerInfo; difficulty={0}, LevelType={1}, GameMode={2}, maxPlayers={3}", new Object[]{info.getDifficulty(), info.getLevelType(), info.getGameMode(), info.getMaxPlayers()});
//    }
//
//    @Override
//    public void onStatusChange(ProtocolStatus status) {
//        logger.log(Level.INFO, "ProtocolStatus={0}", status.name());
//    }
//
//    private static String parseJsonOpt(String str) {
//        if (str.startsWith("{") && str.endsWith("}")) {
//            return parseJsonMessage(new JSONObject(str));
//        } else {
//            return str;
//        }
//    }
//
//    private static String parseJsonMessage(JSONObject object) {
//        return appendJson(new StringBuilder(), object).toString();
//    }
//
//    private static StringBuilder appendJson(StringBuilder messageBuilder, JSONObject object) {
//        if (object.has("text")) {
//            ChatColor color;
//            if (object.has("color")) {
//                color = ChatColor.valueOf(object.getString("color").toUpperCase());
//            } else {
//                color = ChatColor.RESET;
//            }
//            messageBuilder.append(color);
//            if (object.has("text")) {
//                messageBuilder.append(object.getString("text"));
//            }
//        }
//        if (object.has("extra")) {
//            JSONArray array = object.getJSONArray("extra");
//            if (array != null) {
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject tempObj = array.optJSONObject(i);
//                    if (tempObj == null) {
//                        messageBuilder.append(array.getString(i));
//                    } else {
//                        appendJson(messageBuilder, tempObj);
//                    }
//                }
//            }
//        }
//        return messageBuilder;
//    }

    @Override
    public void packetReceived(final PacketReceivedEvent event) {
        logger.log(Level.INFO, "Received {}", event.getPacket());
    }

    @Override
    public void packetSent(final PacketSentEvent event) {
        logger.log(Level.INFO, "Sent {}", event.getPacket());
    }

    @Override
    public void connected(final ConnectedEvent event) {
        logger.log(Level.INFO, "Connected!");
    }

    @Override
    public void disconnecting(final DisconnectingEvent event) {
        logger.log(Level.INFO, "Disconnecting");
    }

    @Override
    public void disconnected(final DisconnectedEvent event) {
        logger.log(Level.INFO, "Disconnected");
    }
}
