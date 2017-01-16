/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.ceylon.launcher;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Fix log format.
 *
 * @author Stephane Epardaud
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class CeylonLogFormatter extends Formatter {
    static final Formatter INSTANCE = new CeylonLogFormatter();
    private static final String MESSAGE_PATTERN = "%s: %s %s\n";

    private CeylonLogFormatter() {
    }

    @Override
    public String format(LogRecord record) {
        //noinspection ThrowableResultOfMethodCallIgnored
        return String.format(
                MESSAGE_PATTERN,
                getErrorType(record.getLevel()),
                record.getMessage(),
                record.getThrown() == null ? "" : record.getThrown());
    }

    private static String getErrorType(Level level) {
        if (level == Level.WARNING)
            return "Warning";
        if (level == Level.INFO)
            return "Note";
        if (level == Level.SEVERE)
            return "Error";
        return "Debug";
    }

}
