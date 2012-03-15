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

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.cmr.spi.OpenNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractRepository implements Repository {

    private OpenNode root;

    public AbstractRepository(OpenNode root) {
        this.root = root;
    }

    private List<String> getDefaultParentPathInternal(ArtifactContext context) {
        final String name = context.getName();
        final List<String> tokens = new ArrayList<String>();
        tokens.addAll(Arrays.asList(name.split("\\.")));
        final String version = context.getVersion();
        if (RepositoryManager.DEFAULT_MODULE.equals(name) == false)
            tokens.add(version); // add version
        return tokens;
    }

    protected List<String> getDefaultParentPath(ArtifactContext context) {
        List<String> tokens = LookupCaching.getTokens();
        if (tokens == null) {
            tokens = getDefaultParentPathInternal(context);
            if (LookupCaching.isEnabled()) {
                LookupCaching.setTokens(tokens);
            }
        }
        return tokens;
    }

    protected static String getArtifactName(String name, String version, String suffix) {
        if (ArtifactContext.DOCS.equals(suffix))
            return ArtifactContext.DOCS;
        else if (RepositoryManager.DEFAULT_MODULE.equals(name))
            return name + suffix;
        else
            return buildArtifactName(name, version, suffix);
    }

    protected static String buildArtifactName(String name, String version, String suffix) {
        return name + "-" + version + suffix;
    }

    public OpenNode getRoot() {
        return root;
    }

    public Node findParent(ArtifactContext context) {
        final List<String> tokens = getDefaultParentPath(context);
        return NodeUtils.getNode(root, tokens);
    }

    public String getArtifactName(ArtifactContext context) {
        return getArtifactName(context.getName(), context.getVersion(), context.getSuffix());
    }

    public OpenNode createParent(ArtifactContext context) {
        final List<String> tokens = getDefaultParentPath(context);
        OpenNode current = root;
        for (String token : tokens) {
            current = current.addNode(token);
        }
        return current;
    }

    protected abstract ArtifactResult getArtifactResultInternal(RepositoryManager manager, Node node);

    public ArtifactResult getArtifactResult(RepositoryManager manager, Node node) {
        return (node != null) ? getArtifactResultInternal(manager, node) : null;
    }

    @Override
    public int hashCode() {
        return root.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Repository == false)
            return false;

        final Repository daca = (Repository) obj;
        return root.equals(daca.getRoot());
    }

    @Override
    public String toString() {
        return "Repository for root: " + root;
    }
    
    @Override
    public String getDisplayString() {
        return root.getDisplayString();
    }
}
