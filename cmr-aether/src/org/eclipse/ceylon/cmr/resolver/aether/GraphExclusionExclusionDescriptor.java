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

import org.eclipse.ceylon.aether.eclipse.aether.graph.Exclusion;

public class GraphExclusionExclusionDescriptor implements ExclusionDescriptor {

    private Exclusion x;

    GraphExclusionExclusionDescriptor(Exclusion x) {
        this.x = x;
    }

    @Override
    public String getGroupId() {
        return x.getGroupId();
    }

    @Override
    public String getArtifactId() {
        return x.getArtifactId();
    }

}
