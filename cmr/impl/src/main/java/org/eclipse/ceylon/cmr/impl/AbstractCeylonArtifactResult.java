

package org.eclipse.ceylon.cmr.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleInfo;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.api.PathFilterParser;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.ArtifactResultType;
import org.eclipse.ceylon.model.cmr.PathFilter;
import org.eclipse.ceylon.model.cmr.Repository;
import org.eclipse.ceylon.model.cmr.RepositoryException;

/**
 * Abstract, use Jandex to read off Module info.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractCeylonArtifactResult extends AbstractArtifactResult {
    private RepositoryManager manager;
    private ModuleInfo infos;
    private boolean resolved = false;

    protected AbstractCeylonArtifactResult(Repository repository, RepositoryManager manager, 
            String name, String version) {
        super(repository, null, name, version);
        this.manager = manager;
    }

    @Override
    public ArtifactResultType type() {
        return ArtifactResultType.CEYLON;
    }

    protected ModuleInfo resolve(){
        if(!resolved){
            Overrides overrides = ((CmrRepository)repository()).getRoot().getService(Overrides.class);
            this.infos = Configuration.getResolvers(manager).resolve(this, overrides);
            resolved = true;
        }
        return infos;
    }

    protected RepositoryManager getManager(){
        return manager;
    }
    
    @Override
    public PathFilter filter(){
        ModuleInfo infos = resolve();
        if(infos == null || infos.getFilter() == null)
            return null;
        try {
            return PathFilterParser.parse(infos.getFilter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<ArtifactResult> dependencies() throws RepositoryException {
        ModuleInfo infos = resolve();
        // TODO -- perhaps null is not valid?
        if (infos == null || infos.getDependencies().isEmpty())
            return Collections.emptyList();

        final List<ArtifactResult> results = new ArrayList<ArtifactResult>();
        for (ModuleDependencyInfo mi : getOrderedDependencies(infos)) {
            results.add(new LazyArtifactResult(manager,
                    mi.getNamespace(),
                    mi.getName(),
                    mi.getVersion(),
                    mi.isExport(),
                    mi.isOptional(),
                    mi.getModuleScope()));
        }
        return results;
    }

    /**
     * {@code ModuleInfo} has dependencies as a {@code Set} (actually a 
     * {@code HashSet}), but {@link #dependencies()} needs to return a list.
     * The order matter much except when several dependencies can't be found.
     * That happens, in particular, when trying to run a module compiled with a 
     * more recent (but still BC) version of Ceylon. So for consistency 
     * on that case we will already return ceylon.language first. 
     */
    private List<ModuleDependencyInfo> getOrderedDependencies(ModuleInfo infos) {
        List<ModuleDependencyInfo> dependencies = new ArrayList<ModuleDependencyInfo>(infos.getDependencies());
        for (int index = 0; index < dependencies.size(); index++) {
            ModuleDependencyInfo dep = dependencies.get(index);
            if ("ceylon.language".equals(dep.getName())) {
                if (index != 0) {
                    dependencies.remove(index);
                    dependencies.add(0, dep);
                }
                break;
            }
        }
        return dependencies;
    }
    
    @Override
    public String groupId() {
        ModuleInfo info = resolve();
        if(info != null){
            String groupId = info.getGroupId();
            if(groupId != null && !groupId.isEmpty()){
                return groupId;
            }
        }
        return ModuleUtil.getMavenCoordinates(name())[0];
    }
    
    @Override
    public String artifactId() {
        ModuleInfo info = resolve();
        if(info != null){
            String groupId = info.getGroupId();
            String artifactId = info.getArtifactId();
            if(groupId != null && !groupId.isEmpty()){
                if(artifactId != null && !artifactId.isEmpty())
                    return artifactId;
                // if we have a group but not artifact, default to the entire name
                return name();
            }
        }
        return ModuleUtil.getMavenCoordinates(name())[1];
    }
    
    @Override
    public String classifier() {
        ModuleInfo info = resolve();
        if(info != null){
            String classifier = info.getClassifier();
            if(classifier != null && !classifier.isEmpty()){
                return classifier;
            }
        }
        return ModuleUtil.getMavenCoordinates(name())[2];
    }
    
}

