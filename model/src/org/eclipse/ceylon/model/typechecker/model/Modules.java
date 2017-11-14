/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents the set of modules involved in the compilation
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Modules {
    private Module languageModule;
    private Set<Module> modules = new TreeSet<Module>();
    private Module defaultModule;

    public Module getLanguageModule() {
        return languageModule;
    }

    public void setLanguageModule(Module languageModule) {
        this.languageModule = languageModule;
    }

    public void setDefaultModule(Module defaultModule) {
        this.defaultModule = defaultModule;
    }

    public Module getDefaultModule() {
        return defaultModule;
    }

    public Set<Module> getListOfModules() {
        return modules;
    }
}
