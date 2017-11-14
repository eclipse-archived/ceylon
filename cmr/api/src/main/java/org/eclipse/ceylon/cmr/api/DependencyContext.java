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
public interface DependencyContext {
    /**
     * Get current artifact,
     * whose dependencies we're trying to read.
     *
     * @return current artifact
     */
    ArtifactResult result();

    /**
     * Do we ignore inner descriptors.
     * e.g. could be used to override inner decriptor
     *
     * @return true if yes, false otherwise
     */
    boolean ignoreInner();

    /**
     * Do we ignore external descriptors.
     * e.g. must be applied when looking at a flat classpath
     *
     * @return true if yes, false otherwise
     */
    boolean ignoreExternal();
}
