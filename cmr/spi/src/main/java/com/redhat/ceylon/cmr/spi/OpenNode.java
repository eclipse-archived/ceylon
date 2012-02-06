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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Mutable node.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface OpenNode extends Node {
    <T> void addService(Class<T> serviceType, T service);

    <T> T getService(Class<T> serviceType);

    void merge(OpenNode other);

    void link(OpenNode other);

    OpenNode peekChild(String label);

    /**
     * Add simple new node.
     * <p/>
     * If there is no need for structure handling use this,
     * otherwise use createNode.
     *
     * @param label the label
     * @return new node
     */
    OpenNode addNode(String label);

    /**
     * Add simple new node.
     * <p/>
     * If there is no need for structure handling use this,
     * otherwise use createNode.
     *
     * @param label the label
     * @param value the value
     * @return new node
     */
    OpenNode addNode(String label, Object value);

    /**
     * Use structure builder to create node.
     *
     * @param label the label
     * @return new node, if it can be created from StructureBuilder
     */
    OpenNode createNode(String label);

    /**
     * Add content.
     *
     * @param label   the node label
     * @param content the node content
     * @param options the options
     * @return new node, or null if cannot add content
     * @throws IOException for any I/O error
     */
    OpenNode addContent(String label, InputStream content, ContentOptions options) throws IOException;

    <T extends Serializable> OpenNode addContent(String label, T content, ContentOptions options) throws IOException;

    Node removeNode(String label);

    /**
     * Refresh; remove markers.
     *
     * @param recurse do we recurse flag
     */
    void refresh(boolean recurse);
}
