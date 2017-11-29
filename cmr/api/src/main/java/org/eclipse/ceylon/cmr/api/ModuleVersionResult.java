/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.api;

import java.util.NavigableMap;
import java.util.TreeMap;

public class ModuleVersionResult {
    private String name;
    private NavigableMap<String, ModuleVersionDetails> versions = new TreeMap<String, ModuleVersionDetails>();

    public ModuleVersionResult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ModuleVersionDetails addVersion(String namespace, String module, String version) {
        if(versions.containsKey(version))
            return null;
        ModuleVersionDetails newVersion = new ModuleVersionDetails(namespace, module, version, null, null); // set up later
        versions.put(version, newVersion);
        return newVersion;
    }

    public ModuleVersionDetails addVersion(ModuleVersionDetails version) {
        if(versions.containsKey(version.getVersion()))
            return null;
        versions.put(version.getVersion(), version);
        return version;
    }

    public NavigableMap<String, ModuleVersionDetails> getVersions() {
        return versions;
    }

    public boolean hasVersion(String version) {
        return versions.containsKey(version);
    }

    @Override
    public String toString() {
        return "ModuleVersionResult[name=" + name + ",versions=" + versions + "]";
    }
    
}
