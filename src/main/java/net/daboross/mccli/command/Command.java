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
package net.daboross.mccli.command;

import java.util.Arrays;
import net.daboross.mccli.log.ChatColor;
import net.daboross.mccli.utils.ArrayUtils;

public abstract class Command {

    protected final String[] aliases;
    private String helpText;
    private String[] helpArgs;

    public Command(String... aliases) {
        this.aliases = aliases;
        Arrays.sort(aliases);
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpArgs(String... helpArgs) {
        this.helpArgs = helpArgs;
    }

    public String[] getHelpArgs() {
        return helpArgs;
    }

    public boolean doesMatch(String alias) {
        return Arrays.binarySearch(aliases, alias) > 0;
    }

    public void sendHelpText(Sender sender) {
        sender.sendMessage(ChatColor.AQUA + ArrayUtils.join(aliases, ChatColor.GRAY + " | " + ChatColor.AQUA)
                + ((helpArgs == null || helpArgs.length == 0) ? "" : " " + ChatColor.GOLD + ArrayUtils.join(helpArgs, " "))
                + ChatColor.WHITE + " - " + ChatColor.GREEN + getHelpText());
    }

    public abstract void run(Sender sender, String commandLabel, String[] args);
}
