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
 * Abstract dependency resolver.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractDependencyResolver implements DependencyResolver {
    @Override
    public ModuleInfo resolve(ArtifactResult result, Overrides overrides) {
        return resolve(new DependencyContextImpl(result, false, false), overrides);
    }
}

