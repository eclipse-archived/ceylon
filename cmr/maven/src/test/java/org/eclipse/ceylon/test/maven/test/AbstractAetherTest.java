/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.test.maven.test;

import java.io.File;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.impl.CMRJULLogger;
import org.eclipse.ceylon.cmr.maven.AetherRepository;

/**
 * Abstract Aether tests.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractAetherTest {
    protected static final org.eclipse.ceylon.common.log.Logger log = new CMRJULLogger();

    AbstractAetherTest() {
        // Force logger to log dammit!
        initLogger(Logger.getLogger("org.eclipse.ceylon.log.cmr"), true);
    }

    protected CmrRepository createAetherRepository() throws Exception {
        URL settingsURL = getClass().getClassLoader().getResource("maven-settings/settings.xml");
        String settingsXml = new File(settingsURL.toURI()).getPath();
        return AetherRepository.createRepository(log, settingsXml, false, 60000, new File("").getAbsolutePath());
    }
    
    protected static void initLogger(Logger logger, boolean verbose) {
        boolean handlersExists = false;
        for (Handler handler : logger.getHandlers()) {
            handlersExists = true;
            if (handler instanceof ConsoleHandler) {
                if (verbose) {
                    handler.setLevel(Level.ALL);
                }
            }
        }
        if (verbose) {
            logger.setLevel(Level.ALL);
            if (handlersExists == false) {
                ConsoleHandler handler = new ConsoleHandler();
                handler.setLevel(Level.ALL);
                logger.addHandler(handler);
            }
        }
    }
}
