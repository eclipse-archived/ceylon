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

import com.redhat.ceylon.cmr.spi.ContentStore;
import com.redhat.ceylon.cmr.spi.StructureBuilder;

/**
 * Root node impl.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RootNode extends DefaultNode {
    public RootNode(ContentStore contentStore, StructureBuilder structureBuilder) {
        super("");
        addService(ContentStore.class, contentStore);
        addService(StructureBuilder.class, structureBuilder);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this); // should be only one
    }

    @Override
    public String toString() {
        return "RootNode for "+getService(ContentStore.class);
    }
    
    @Override
    public String getDisplayString() {
        return getService(ContentStore.class).getDisplayString();
    }
}
