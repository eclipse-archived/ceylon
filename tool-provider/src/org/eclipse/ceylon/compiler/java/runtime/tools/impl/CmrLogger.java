/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.tools.impl;

import org.eclipse.ceylon.common.log.Logger;

public class CmrLogger implements Logger {

    private boolean debugEnabled;

    public CmrLogger(boolean verbose) {
        debugEnabled = verbose;
    }

    @Override
    public void error(String str) {
        System.err.println("[CMR:ERROR] "+str);
    }

    @Override
    public void warning(String str) {
        System.err.println("[CMR:WARNING] "+str);
    }

    @Override
    public void info(String str) {
        System.err.println("[CMR:INFO] "+str);
    }

    @Override
    public void debug(String str) {
        if(debugEnabled)
            System.err.println("[CMR:DEBUG] "+str);
    }

}
