package com.redhat.ceylon.model.loader;

import com.redhat.ceylon.common.log.JULLogger;

/**
 * Simple logger impl.
 *
 * @author Stephane Epardaud, Tako Schotanus
 */
public class LoaderJULLogger extends JULLogger {

    static java.util.logging.Logger log = java.util.logging.Logger.getLogger("com.redhat.ceylon.log.loader");

    @Override
    protected java.util.logging.Logger logger() {
        return log;
    }

}
