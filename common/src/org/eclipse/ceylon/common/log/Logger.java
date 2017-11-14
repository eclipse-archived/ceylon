

package org.eclipse.ceylon.common.log;

/**
 * Simple logger abstraction.
 *
 * @author Stephane Epardaud
 */
public interface Logger {
    void error(String str);

    void warning(String str);

    void info(String str);

    void debug(String str);
}
