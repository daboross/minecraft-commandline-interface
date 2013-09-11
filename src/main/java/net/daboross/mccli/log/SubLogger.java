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
package net.daboross.mccli.log;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author Dabo Ross <http://www.daboross.net/>
 */
public class SubLogger extends Logger {

    private final String name;
    private final Logger parent;

    public SubLogger(String name, Logger parent) {
        super("Client." + name, null);
        this.name = "[" + name + "] ";
        this.parent = parent;
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage(name + record.getMessage());
        parent.log(record);
    }

    public SubLogger getSubLogger(String name) {
        return new SubLogger(name, this);
    }
}
