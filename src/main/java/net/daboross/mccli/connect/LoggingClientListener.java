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
import org.json.JSONException;
import org.json.JSONObject;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSentEvent;
import com.github.steveice10.packetlib.event.session.SessionListener;
import com.github.steveice10.packetlib.packet.Packet;

public class LoggingClientListener implements SessionListener {

    private final Logger logger;

    public LoggingClientListener(Logger logger) {
        this.logger = logger;
    }

    private static String parseJsonOpt(String str) {
        if (str.startsWith("{") && str.endsWith("}")) {
            return parseJsonMessage(new JSONObject(str));
        } else if (str.startsWith("\"") && str.endsWith("\"")) {
            str = str.substring(1, str.length() - 1).replaceAll("\\\\\"", "\"");
            return parseJsonOpt(str);
        } else {
            return str;
        }
    }

    private static String parseJsonMessage(JSONObject object) {
        return appendJson(new StringBuilder(), object).toString();
    }

    private static StringBuilder appendJson(StringBuilder messageBuilder, JSONObject object) {
        if (object.has("text")) {
            ChatColor color;
            if (object.has("color")) {
                color = ChatColor.valueOf(object.getString("color").toUpperCase());
            } else {
                color = ChatColor.RESET;
            }
            messageBuilder.append(color);
            if (object.has("text")) {
                messageBuilder.append(object.getString("text"));
            }
        }
        if (object.has("extra")) {
            JSONArray array = object.getJSONArray("extra");
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject tempObj = array.optJSONObject(i);
                    if (tempObj == null) {
                        messageBuilder.append(array.getString(i));
                    } else {
                        appendJson(messageBuilder, tempObj);
                    }
                }
            }
        }
        return messageBuilder;
    }

    @Override
    public void packetReceived(final PacketReceivedEvent event) {
        Packet packet = event.getPacket();
        if (packet instanceof ServerJoinGamePacket) {
            logger.log(Level.INFO, "Joined game!");
        } else if (packet instanceof ServerChatPacket) {
            Message message = event.<ServerChatPacket>getPacket().getMessage();
            try {
                logger.log(Level.INFO, "[Chat] {0}", parseJsonOpt(message.toJsonString()));
            } catch (JSONException ex) {
                logger.log(Level.SEVERE, "Failed to get chat text", ex);
            }
        } else if (packet instanceof LoginDisconnectPacket) {
            logger.log(Level.INFO, "Disconnected: " + ((LoginDisconnectPacket) packet).getReason());
        }
//        else if (!(packet instanceof ServerEntityHeadLookPacket || packet instanceof ServerEntityEquipmentPacket
//                || packet instanceof ServerEntityPropertiesPacket || packet instanceof ServerSpawnMobPacket
//                || packet instanceof ServerEntityVelocityPacket || packet instanceof ServerEntityRotationPacket
//                || packet instanceof ServerEntityPositionRotationPacket || packet instanceof ServerEntityTeleportPacket
//                || packet instanceof ServerEntityPositionPacket || packet instanceof ServerMultiChunkDataPacket
//                || packet instanceof ServerDestroyEntitiesPacket || packet instanceof ServerBlockChangePacket
//                || packet instanceof ServerEntityMetadataPacket || packet instanceof ServerSpawnObjectPacket
//                || packet instanceof ServerUpdateTimePacket || packet instanceof ServerPlaySoundPacket
//                || packet instanceof ServerMultiBlockChangePacket || packet instanceof ServerKeepAlivePacket
//                || packet instanceof ServerUpdateTileEntityPacket)) {
//            logger.log(Level.INFO, "Received {0}", event.getPacket());
//        }
    }

//    @Override
//    public void onKick(String reason) {
//        logger.log(Level.INFO, "Kicked for {0}", parseJsonOpt(reason));
//    }

    @Override
    public void packetSent(final PacketSentEvent event) {
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
