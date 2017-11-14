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

import org.eclipse.ceylon.model.cmr.RuntimeResolver;
import org.eclipse.ceylon.model.typechecker.model.Module;

/**
 * Implements {@link RuntimeResolver} using {@link Overrides}.
 */
public class OverridesRuntimeResolver implements org.eclipse.ceylon.model.cmr.RuntimeResolver {

    private Overrides overrides;

    public OverridesRuntimeResolver(Overrides overrides) {
        this.overrides = overrides;
    }
    
    private String findOverride(String name, String version) {
        final ArtifactContext context = new ArtifactContext(null, name, version, ArtifactContext.CAR, ArtifactContext.JAR);
        ArtifactContext override = overrides.applyOverrides(context);
        return override.getVersion();
    }
    
    @Override
    public String resolveVersion(String moduleName, String moduleVersion) {
        if (Module.DEFAULT_MODULE_NAME.equals(moduleName)) {
            // JBoss Modules turns default/null into default:main
            return null;
        }
        return findOverride(moduleName, moduleVersion);
    }
}
