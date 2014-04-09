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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

/**
 * Maven repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class MavenRepository extends AbstractRepository {
    protected MavenRepository(OpenNode root) {
        super(root);
    }

    @Override
    public String[] getArtifactNames(ArtifactContext context) {
        String name = context.getName();
        final int p = name.lastIndexOf(".");
        return getArtifactNames(p >= 0 ? name.substring(p + 1) : name, context.getVersion(), context.getSuffixes());
    }

    @Override
    protected ArtifactResult getArtifactResultInternal(RepositoryManager manager, final Node node) {
        ArtifactContext context = ArtifactContext.fromNode(node);
        return new MavenArtifactResult(manager, context.getName(), context.getVersion(), node);
    }

    @Override
    public String getDisplayString() {
        return "[Maven] " + super.getDisplayString();
    }

    private static class MavenArtifactResult extends AbstractCeylonArtifactResult {
        private Node node;

        private MavenArtifactResult(RepositoryManager manager, String name, String version, Node node) {
            super(manager, name, version);
            this.node = node;
        }

        @Override
        public ArtifactResultType type() {
            return ArtifactResultType.MAVEN;
        }

        @Override
        protected File artifactInternal() throws RepositoryException {
            try {
                return node.getContent(File.class);
            } catch (IOException e) {
                throw new RepositoryException(e);
            }
        }

        @Override
        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.emptyList(); // dunno how to grab deps
        }
        
        @Override
        public String repositoryDisplayString() {
            return NodeUtils.getRepositoryDisplayString(node);
        }
    }
}
