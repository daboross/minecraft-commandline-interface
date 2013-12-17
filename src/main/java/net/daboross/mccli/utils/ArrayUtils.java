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
package net.daboross.mccli.utils;

public class ArrayUtils {

    public static String join(Object[] objects, String seperator) {
        if (objects == null || objects.length == 0) {
            return "";
        }
        StringBuilder build = new StringBuilder(String.valueOf(objects[0]));
        for (int i = 1; i < objects.length; i++) {
            build.append(seperator).append(objects[i]);
        }
        return build.toString();
    }

    public static String join(Object[] objects, int start, String seperator) {
        if (objects == null || objects.length <= start) {
            return "";
        }
        StringBuilder build = new StringBuilder(String.valueOf(objects[start]));
        for (int i = start + 1; i < objects.length; i++) {
            build.append(seperator).append(objects[i]);
        }
        return build.toString();
    }

    public static String join(Object[] objects) {
        return join(objects, " ");
    }

    public static String[] sub(String[] args) {
        String[] sub = new String[args.length - 1];
        System.arraycopy(args, 1, sub, 0, args.length - 1);
        return sub;
    }
}
