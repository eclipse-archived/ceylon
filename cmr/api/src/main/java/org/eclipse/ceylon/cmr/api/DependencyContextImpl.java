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

import org.eclipse.ceylon.model.cmr.ArtifactResult;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class DependencyContextImpl implements DependencyContext {
    private final ArtifactResult result;
    private boolean ignoreInner;
    private boolean ignoreExternal;

    DependencyContextImpl(ArtifactResult result, boolean ignoreInner, boolean ignoreExternal) {
        this.result = result;
        this.ignoreInner = ignoreInner;
        this.ignoreExternal = ignoreExternal;
    }

    @Override
    public ArtifactResult result() {
        return result;
    }

    public boolean ignoreInner() {
        return ignoreInner;
    }

    public boolean ignoreExternal() {
        return ignoreExternal;
    }
}
