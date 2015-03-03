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

import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ModuleQuery.Type;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.Repository;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.MavenRepository;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.log.Logger;

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

    public static Repository createRepository(Logger log, boolean offline, int timeout) {
        return createRepository(log, null, offline, timeout);
    }

    public static Repository createRepository(Logger log, String settingsXml, boolean offline, int timeout) {
        AetherContentStore acs = new AetherContentStore(log, offline, timeout);
        AetherRepository repo = new AetherRepository(acs);
        repo.utils.overrideSettingsXml(settingsXml);
        return repo;
    }

    @Override
    public String[] getArtifactNames(ArtifactContext context) {
        String name = context.getName();
        final int p = name.contains(":") ? name.lastIndexOf(":") : name.lastIndexOf(".");

        return getArtifactNames(p >= 0 ? name.substring(p + 1) : name, context.getVersion(), context.getSuffixes());
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

    @Override
    protected List<String> getDefaultParentPathInternal(ArtifactContext context) {
        return MavenRepository.getParentPath(context);
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
    
    @Override
    public void completeVersions(ModuleVersionQuery lookup, ModuleVersionResult result) {
        if(lookup.getType() != Type.ALL && lookup.getType() != null){
            boolean ok = false;
            for(String suffix : lookup.getType().getSuffixes()){
                if(suffix.equals(ArtifactContext.JAR)){
                    ok = true;
                    break;
                }
            }
            if(!ok)
                return;
        }
        // this means only for explicitly Maven modules that have a ":"
        if(lookup.getName().indexOf(':') == -1)
            return;
        String[] groupArtifactIds = utils.nameToGroupArtifactIds(lookup.getName());
        if(groupArtifactIds == null)
            return;
        // FIXME: does not respect paging or count
        utils.search(groupArtifactIds[0], groupArtifactIds[1], lookup.getVersion(), result, getOverrides(), getDisplayString());
    }
}
