/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.io.cmr.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.common.log.Logger;

/**
 * Logger that exposes its logs with programmatic API and let them be reset
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class LeakingLogger implements Logger {
    private List<String> errors = new ArrayList<String>();
    private List<String> warnings = new ArrayList<String>();
    //grammatically incorrect but more regular in the codebase
    private List<String> infos = new ArrayList<String>();
    private List<String> debugs = new ArrayList<String>();

    @Override
    public void error(String s) {
        errors.add(s);
    }

    @Override
    public void warning(String s) {
        warnings.add(s);
    }

    @Override
    public void info(String s) {
        infos.add(s);
    }

    @Override
    public void debug(String s) {
        debugs.add(s);
    }

    public void clear() {
        debugs.clear();
        infos.clear();
        warnings.clear();
        errors.clear();
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public List<String> getInfos() {
        return infos;
    }

    public List<String> getDebugs() {
        return debugs;
    }
}
