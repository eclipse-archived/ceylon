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

package com.redhat.ceylon.cmr.impl;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.spi.OpenNode;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

import java.io.File;
import java.net.URI;

/**
 * Root builder.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RootBuilder {

    private StructureBuilder structureBuilder;

    public RootBuilder(String token, Logger log) throws Exception {
        if (token == null)
            throw new IllegalArgumentException("Null root token");

        final String key = (token.startsWith("${") ? token.substring(2, token.length() - 1) : token);
        final String temp = SecurityActions.getProperty(key);
        if (temp != null)
            token = temp;

        if (token.startsWith("http")) {
            structureBuilder = new RemoteContentStore(token, log);
        } else {
            final File file = (token.startsWith("file") ? new File(new URI(token)) : new File(token));
            if (file.exists() == false)
                throw new IllegalArgumentException("Token does not point to an existing directory: " + token);
            if (file.isDirectory() == false)
                throw new IllegalArgumentException("Token does not point to a directory: " + token);

            structureBuilder = new FileContentStore(file);
        }
    }

    public OpenNode buildRoot() {
        return structureBuilder.createRoot();
    }
}
