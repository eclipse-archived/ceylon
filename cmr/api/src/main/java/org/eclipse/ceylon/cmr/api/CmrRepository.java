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

import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.Repository;

/**
 * Repository -- prepare artifact context per custom repo.
 * e.g. Maven has different naming for artifacts + default suffix is .jar
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface CmrRepository extends Repository, ContentFinder {

    /**
     * Get root node.
     *
     * @return the root node
     */
    OpenNode getRoot();

    /**
     * Find parent node for context.
     *
     * @param context the context
     * @return parent node or null if cannot be found
     */
    Node findParent(ArtifactContext context);

    /**
     * Get artifact names.
     *
     * @param context the context
     * @return the array of possible artifact names
     */
    String[] getArtifactNames(ArtifactContext context);

    /**
     * Create parent.
     *
     * @param context the context
     * @return create parent node
     */
    OpenNode createParent(ArtifactContext context);

    /**
     * Get artifact result for node.
     *
     * @param manager the node's owning manager
     * @param node    the node
     * @return node's artifact result
     */
    ArtifactResult getArtifactResult(RepositoryManager manager, Node node);

    /**
     * Makes sure that content cached as "unavailable" is reasessed
     */
    void refresh(boolean recurse);

    boolean supportsNamespace(String namespace);
}
