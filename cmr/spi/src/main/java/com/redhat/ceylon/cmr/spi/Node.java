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
 * Represent repository contents as nodes.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface Node {
    String getLabel();

    <T> T getValue(Class<T> valueType);

    Node getChild(String label);

    Iterable<? extends Node> getChildren();

    boolean hasBinaries();

    InputStream getInputStream() throws IOException;

    <T> T getContent(Class<T> contentType) throws IOException;

    long getLastModified() throws IOException;

    long getSize() throws IOException;

    Node getParent(String label);

    Iterable<? extends Node> getParents();

    boolean isRemote();

    String getDisplayString();
}
