/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

public class ModuleVersionQuery extends ModuleQuery {

    private String version;
    private boolean exactVersionMatch;

    public ModuleVersionQuery(String name, String version, Type type) {
        super(null, name, type);
        this.version = version;
    }

    public ModuleVersionQuery(String namespace, String name, String version, Type type) {
        super(namespace, name, type);
        this.version = version;
    }

    public void setExactVersionMatch(boolean exactVersionMatch) {
        this.exactVersionMatch = exactVersionMatch;
    }
    
    public boolean isExactVersionMatch() {
        return exactVersionMatch;
    }
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ModuleVersionQuery[ns=" + namespace + ",name=" + name + ",version=" + version + ",type=" + type + "]";
    }
}
