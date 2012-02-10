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

package com.redhat.ceylon.cmr.maven;

import com.redhat.ceylon.cmr.spi.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * Sonatype Aether content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherContentStore implements ContentStore, StructureBuilder {

    public ContentHandle peekContent(Node node) {
        return null;
    }

    public ContentHandle getContent(Node node) throws IOException {
        return null;
    }

    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        return null;
    }

    public OpenNode createRoot() {
        return null;
    }

    public OpenNode create(Node parent, String child) {
        return null;
    }

    public OpenNode find(Node parent, String child) {
        return null;
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        return null;
    }

}
