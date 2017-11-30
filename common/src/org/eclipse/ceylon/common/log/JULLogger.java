/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.log;

/**
 * Simple logger impl.
 *
 * @author Stephane Epardaud
 */
public abstract class JULLogger implements Logger {

    protected abstract java.util.logging.Logger logger();
    
    @Override
    public void error(String str) {
        logger().severe(str);
    }

    @Override
    public void warning(String str) {
        logger().warning(str);
    }

    @Override
    public void info(String str) {
        logger().info(str);
    }

    @Override
    public void debug(String str) {
        logger().fine(str);
    }

}
