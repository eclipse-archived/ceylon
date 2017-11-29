/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.spi;

import java.io.File;
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
    
    int getTimeout();
    
    boolean isOffline();
    
    boolean isHerd();
    
    boolean canHandleFolders();
    
    Iterable<File> getBaseDirectories();
}
