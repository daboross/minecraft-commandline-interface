package net.daboross.mccli.log;

import org.json.JSONArray;
import org.json.JSONObject;

public class Parser {

    public static String parseJson(JSONObject object) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(object.getString("text")).append(" ");
        JSONArray array = object.getJSONArray("extra");
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tempObj = array.optJSONObject(i);
                if (tempObj == null) {
                    messageBuilder.append(array.getString(i));
                } else {
                    ChatColor color = ChatColor.valueOf(array.getJSONObject(i).getString("color").toUpperCase());
                    messageBuilder.append(color);
                    messageBuilder.append(array.getJSONObject(i).getString("text"));
                }
            }
        }
        return messageBuilder.toString();
    }
}
