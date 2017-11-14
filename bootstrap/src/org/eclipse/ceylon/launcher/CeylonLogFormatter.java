
package org.eclipse.ceylon.launcher;

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
