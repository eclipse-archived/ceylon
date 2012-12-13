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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.MavenRepository;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.cmr.spi.Node;

/**
 * Aether repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherRepository extends MavenRepository {
    private final AetherUtils utils;

    private AetherRepository(AetherContentStore acs) {
        super(acs.createRoot());
        utils = acs.getUtils();
    }

    public static Repository createRepository(Logger log) {
        AetherContentStore acs = new AetherContentStore(log);
        return new AetherRepository(acs);
    }

    public static Repository createRepository(Logger log, String settingsXml) {
        AetherContentStore acs = new AetherContentStore(log);
        AetherRepository repo = new AetherRepository(acs);
        repo.utils.overrideSettingsXml(settingsXml);
        return repo;
    }

    @Override
    public String getArtifactName(ArtifactContext context) {
        String name = context.getName();
        final int p = name.contains(":") ? name.lastIndexOf(":") : name.lastIndexOf(".");

        return getArtifactName(p >= 0 ? name.substring(p + 1) : name, context.getVersion(), ArtifactContext.JAR);
    }

    @Override
    protected String toModuleName(Node node) {
        ArtifactContext context = ArtifactContext.fromNode(node);
        if (context != null) {
            return context.getName();
        }
        String moduleName = node.getLabel();
        Node parent = NodeUtils.firstParent(node);
        String groupId = NodeUtils.getFullPath(parent, ".");
        // That's sort of an invariant, but let's be safe
        if (groupId.startsWith("."))
            groupId = groupId.substring(1);
        moduleName = groupId != null ? groupId + ":" + moduleName : moduleName;
        return moduleName;
    }

    protected List<String> getDefaultParentPathInternal(ArtifactContext context) {
        final String name = context.getName();
        final int p = name.contains(":") ? name.lastIndexOf(":") : name.lastIndexOf(".");
        final List<String> tokens = new ArrayList<String>();
        if (p == -1) {
            tokens.addAll(Arrays.asList(name.split("\\.")));
        } else {
            tokens.addAll(Arrays.asList(name.substring(0, p).split("\\.")));
            tokens.add(name.substring(p + 1));
        }
        final String version = context.getVersion();
        if (RepositoryManager.DEFAULT_MODULE.equals(name) == false && version != null)
            tokens.add(version); // add version
        return tokens;
    }

    @Override
    public Node findParent(ArtifactContext context) {
        if (context.getName().startsWith("ceylon.")) {
            return null;
        }
        return super.findParent(context);
    }

    public ArtifactResult getArtifactResultInternal(RepositoryManager manager, Node node) {
        return utils.findDependencies(node);
    }
}
