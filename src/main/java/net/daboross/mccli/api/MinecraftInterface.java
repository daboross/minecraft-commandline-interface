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
package net.daboross.mccli.api;

import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import net.daboross.mccli.clients.CurrentlyRunningClientsMap;
import net.daboross.mccli.command.Sender;
import net.daboross.mccli.input.InputHandlerThread;
import net.daboross.mccli.log.SubLogger;

public interface MinecraftInterface {

    public Logger getLogger();

    public InputHandlerThread getInput();

    public SubLogger getSubLogger(String name);

    public void dispatchCommand(Sender sender, String command, String args);

    public CurrentlyRunningClientsMap getClients();

    public ExecutorService getExecutor();

    public void stop();
}
