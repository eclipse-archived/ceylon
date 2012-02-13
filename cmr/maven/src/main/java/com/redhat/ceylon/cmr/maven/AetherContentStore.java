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
import com.redhat.ceylon.cmr.impl.RemoteContentStore;
import com.redhat.ceylon.cmr.spi.ContentHandle;
import com.redhat.ceylon.cmr.spi.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Sonatype Aether content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherContentStore extends RemoteContentStore {

    public AetherContentStore(String root, Logger log) {
        super(root, log);
    }

    protected ContentHandle createContentHandle(Node parent, String child, String path, Node node) {
        return new AetherContentHandle(node);
    }

    private static class AetherContentHandle implements ContentHandle {
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

            final String name = ac.getName();
            final int p = name.lastIndexOf(".");
            final String groupId = name.substring(0, p);
            final String artifactId = name.substring(p + 1);
            final String version = ac.getVersion();

            final File dependency = AetherUtils.getDependency(groupId, artifactId, version);
            if (dependency == null || dependency.exists() == false)
                throw new IOException("Invalid dependency: " + ac);

            return dependency;
        }

        public void clean() {
        }
    }
}
