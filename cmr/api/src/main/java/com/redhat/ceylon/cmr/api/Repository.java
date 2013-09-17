/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.api;

import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

/**
 * Repository -- prepare artifact context per custom repo.
 * e.g. Maven has different naming for artifacts + default suffix is .jar
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Repository extends ContentFinder {

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
     * Returns a display string that represents this Repository
     */
    String getDisplayString();

    /**
     * Makes sure that content cached as "unavailable" is reasessed
     */
    void refresh(boolean recurse);
}
