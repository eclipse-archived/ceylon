/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.resolver.aether;

import java.io.File;
import java.util.List;

import org.eclipse.ceylon.aether.apache.maven.model.Dependency;
import org.eclipse.ceylon.aether.apache.maven.model.Exclusion;
import org.eclipse.ceylon.aether.eclipse.aether.util.artifact.JavaScopes;

public class DependencyDependencyDescriptor implements DependencyDescriptor {

    private Dependency model;
    private List<ExclusionDescriptor> exclusions;

    DependencyDependencyDescriptor(Dependency model) {
        this.model = model;
        for(Exclusion x : model.getExclusions()){
            exclusions.add(new ExclusionExclusionDescriptor(x));
        }
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public List<DependencyDescriptor> getDependencies() {
        return null;
    }

    @Override
    public String getGroupId() {
        return model.getGroupId();
    }

    @Override
    public String getArtifactId() {
        return model.getArtifactId();
    }
    
    @Override
    public String getClassifier() {
        return model.getClassifier();
    }

    @Override
    public String getVersion() {
        return model.getVersion();
    }

    @Override
    public boolean isOptional() {
        return model.isOptional();
    }

    @Override
    public boolean isProvidedScope() {
        return JavaScopes.PROVIDED.equals(model.getScope());
    }

    @Override
    public boolean isRuntimeScope() {
        return JavaScopes.RUNTIME.equals(model.getScope());
    }
    
    @Override
    public boolean isCompileScope() {
        return JavaScopes.COMPILE.equals(model.getScope());
    }
    
    @Override
    public boolean isTestScope() {
        return JavaScopes.TEST.equals(model.getScope());
    }

    @Override
    public List<ExclusionDescriptor> getExclusions() {
        return exclusions;
    }

}
