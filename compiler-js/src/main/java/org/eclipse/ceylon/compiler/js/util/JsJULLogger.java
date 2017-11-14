

package org.eclipse.ceylon.compiler.js.util;

import org.eclipse.ceylon.common.log.JULLogger;

/**
 * Simple logger impl.
 *
 * @author Stephane Epardaud, Tako Schotanus
 */
public class JsJULLogger extends JULLogger {

    static java.util.logging.Logger log = java.util.logging.Logger.getLogger("org.eclipse.ceylon.log.js");

    @Override
    protected java.util.logging.Logger logger() {
        return log;
    }

}
