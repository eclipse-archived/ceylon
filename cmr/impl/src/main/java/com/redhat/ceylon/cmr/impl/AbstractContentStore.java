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

package com.redhat.ceylon.cmr.impl;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.spi.ContentStore;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

/**
 * Abstract content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractContentStore implements ContentStore, StructureBuilder {

    private static final String CAR = ".car";
    private static final String JAR = ".jar";
    private static final String PROPERTIES = ".properties";
    private static final String SHA1 = ".sha1";
    private static final String SRC = ".src";
    private static final String XML = ".xml";
    private static final String ZIP = ".zip";

    protected static final String SEPARATOR = "/";

    protected final Set<String> suffixes = new HashSet<String>();
    protected Logger log;

    protected AbstractContentStore(Logger log) {
        this.log = log;
        addSuffix(CAR);
        addSuffix(JAR);
        addSuffix(PROPERTIES);
        addSuffix(SHA1);
        addSuffix(SRC);
        addSuffix(XML);
        addSuffix(ZIP);
    }

    public void addSuffix(String suffix) {
        suffixes.add(suffix);
    }

    protected static String getFullPath(Node parent, String child) {
        final StringBuilder sb = new StringBuilder(NodeUtils.getFullPath(parent, SEPARATOR));
        if (parent.hasBinaries() == false)
            sb.append(SEPARATOR);
        sb.append(child);
        return sb.toString();
    }

    protected boolean hasContent(String child) {
        for (String suffix : suffixes)
            if (child.endsWith(suffix))
                return true;
        return false;
    }
}
