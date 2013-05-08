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

/**
 * Content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface ContentStore {

    /**
     * Just check if the content is there for node.
     *
     * @param node the node to check
     * @return content handle or null if content doesn't exist
     */
    ContentHandle peekContent(Node node);

    /**
     * Get the content handle.
     * Throw IO exception if the content is not there.
     *
     * @param node the node to check
     * @return content handle
     * @throws IOException if content is not found or any other I/O error
     */
    ContentHandle getContent(Node node) throws IOException;

    /**
     * Put content for node.
     *
     * @param node    the node to put against
     * @param stream  the content stream
     * @param options the options
     * @return content handle
     * @throws IOException for any I/O error
     */
    ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException;

    String getDisplayString();
    
    boolean isOffline();
    
    boolean isHerd();
}
