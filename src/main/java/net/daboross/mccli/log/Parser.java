package net.daboross.mccli.log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by portalBlock on 4/27/14.
 */
public class Parser {

    public static String parseJson(JSONObject object){
        String message = "";
        message+= object.getString("text")+" ";
        JSONArray array = object.getJSONArray("extra");
        if(array != null){
            for(int i = 0; i < array.length(); i++){
                JSONObject tempObj = array.optJSONObject(i);
                if(tempObj == null){
                    message+= array.getString(i);
                }else{
                    ChatColor color = ChatColor.valueOf(array.getJSONObject(i).getString("color").toUpperCase());
                    message+= color;
                    message+= array.getJSONObject(i).getString("text");
                }
            }
        }
        return message;
    }

}
