/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
