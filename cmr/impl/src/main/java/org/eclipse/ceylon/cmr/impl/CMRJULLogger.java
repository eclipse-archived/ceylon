

package org.eclipse.ceylon.cmr.impl;

import org.eclipse.ceylon.common.log.JULLogger;

/**
 * Simple logger impl.
 *
 * @author Stephane Epardaud, Tako Schotanus
 */
public class CMRJULLogger extends JULLogger {

    static java.util.logging.Logger log = java.util.logging.Logger.getLogger("org.eclipse.ceylon.log.cmr");

    @Override
    protected java.util.logging.Logger logger() {
        return log;
    }

}
