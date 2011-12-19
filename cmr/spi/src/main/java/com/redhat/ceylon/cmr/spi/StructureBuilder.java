/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
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

package com.redhat.ceylon.cmr.spi;

/**
 * Build graph structure.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface StructureBuilder {

    /**
     * Get root node.
     *
     * @return root node
     */
    OpenNode createRoot();

    /**
     * Find child from parent.
     *
     * @param parent the parent
     * @param child  the child
     * @return found child, or null if it doesn't exist
     */
    OpenNode find(Node parent, String child);

    /**
     * Find all children.
     *
     * @param parent the parent
     * @return all parent's children
     */
    Iterable<? extends OpenNode> find(Node parent);

}
