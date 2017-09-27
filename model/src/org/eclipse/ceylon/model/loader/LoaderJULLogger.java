package org.eclipse.ceylon.model.loader;

import org.eclipse.ceylon.common.log.JULLogger;

/**
 * Simple logger impl.
 *
 * @author Stephane Epardaud, Tako Schotanus
 */
public class LoaderJULLogger extends JULLogger {

    static java.util.logging.Logger log = java.util.logging.Logger.getLogger("org.eclipse.ceylon.log.loader");

    @Override
    protected java.util.logging.Logger logger() {
        return log;
    }

}
