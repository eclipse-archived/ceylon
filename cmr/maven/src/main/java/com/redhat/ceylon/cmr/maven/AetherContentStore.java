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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.impl.AbstractContentStore;
import com.redhat.ceylon.cmr.impl.DefaultNode;
import com.redhat.ceylon.cmr.impl.RootNode;
import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.ContentOptions;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

/**
 * Sonatype Aether content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherContentStore extends AbstractContentStore {

    private final AetherUtils utils;

    public AetherContentStore(Logger log, boolean offline) {
        super(log, offline);
        utils = new AetherUtils(log, offline);
    }

    AetherUtils getUtils() {
        return utils;
    }

    public OpenNode createRoot() {
        return new RootNode(this, this);
    }

    public OpenNode find(Node parent, String child) {
        DefaultNode node = null;
        if (hasContent(child) == false) {
            node = new DefaultNode(child);
            node.setContentMarker();
        } else {
            final File dependency = utils.findDependency(parent);
            if (dependency != null) {
                node = new DefaultNode(child);
                node.setHandle(new FileContentHandle(dependency));
            }
        }
        return node;
    }

    public ContentHandle peekContent(Node node) {
        final File dependency = utils.findDependency(node);
        return (dependency != null) ? new FileContentHandle(dependency) : null;
    }

    public ContentHandle getContent(Node node) throws IOException {
        return new AetherContentHandle(node);
    }

    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        return null;  // cannot put content
    }

    public OpenNode create(Node parent, String child) {
        return null; // cannot create
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        return Collections.emptyList(); // cannot find all children
    }

    private static class FileContentHandle implements ContentHandle {
        private final File file;

        private FileContentHandle(File file) {
            this.file = file;
        }

        public boolean hasBinaries() {
            return true;
        }

        public InputStream getBinariesAsStream() throws IOException {
            return new FileInputStream(file);
        }

        public File getContentAsFile() throws IOException {
            return file;
        }

        public long getLastModified() throws IOException {
            return file.lastModified();
        }

        public void clean() {
        }
    }

    private class AetherContentHandle implements ContentHandle {
        private Node node;

        private AetherContentHandle(Node node) {
            this.node = node;
        }

        public boolean hasBinaries() {
            return true;
        }

        public InputStream getBinariesAsStream() throws IOException {
            return new FileInputStream(getContentAsFile());
        }

        public File getContentAsFile() throws IOException {
            final ArtifactContext ac = ArtifactContext.fromNode(node);
            if (ac == null)
                throw new IOException("Missing artifact context info!");

            return utils.findDependency(node);
        }

        public long getLastModified() throws IOException {
            return getContentAsFile().lastModified();
        }

        public void clean() {
        }
    }

    public String getDisplayString() {
        String name = "Aether";
        if (offline) {
            name += " (offline)";
        }
        return name;
    }

    public boolean isHerd() {
        return false;
    }
}
