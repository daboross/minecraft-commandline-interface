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
package net.daboross.mccli.input;

//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.ListIterator;
//import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.daboross.mccli.log.ChatColor;

/**
 *
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class InputParser {

    private final Pattern pattern = Pattern.compile(".*\\{.*\\.\\..*\\}");

    public String parse(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);
//        Matcher m = pattern.matcher(text);
//        if (m.find()) {
//            ArrayList<String> words = new ArrayList<>(Arrays.asList(text.split(" ")));
//            ListIterator<String> i = words.listIterator();
//            
//            for (; i.hasNext();) {
//                String word = i.next();
//                
//                m.appendReplacement(null, text)
//            }
//            text = text.replaceAll(".*\\{.*\\.\\..*\\}", "");
//        }
        return text;
    }
}
