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

public interface DependencyDescriptor {
    public File getFile();

    public List<DependencyDescriptor> getDependencies();
    public List<ExclusionDescriptor> getExclusions();

    public String getGroupId();
    public String getArtifactId();
    public String getClassifier();
    public String getVersion();
    public boolean isOptional();
    
    public boolean isProvidedScope();
    public boolean isCompileScope();
    public boolean isRuntimeScope();
    public boolean isTestScope();
}