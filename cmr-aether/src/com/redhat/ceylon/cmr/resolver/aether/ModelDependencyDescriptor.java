package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.aether.apache.maven.model.Dependency;
import com.redhat.ceylon.aether.apache.maven.model.Model;
import com.redhat.ceylon.aether.apache.maven.model.Parent;

public class ModelDependencyDescriptor implements DependencyDescriptor {

	private Model model;
    private List<DependencyDescriptor> deps;

	ModelDependencyDescriptor(Model model) {
		this.model = model;
	    deps = new ArrayList<>(model.getDependencies().size());
	    for(Dependency dep : model.getDependencies()){
	        String depScope = dep.getScope();
            // keep compile, runtime, provided
            if(depScope != null 
                    && (depScope.equals("test") || depScope.equals("system")))
                continue;
	        deps.add(new DependencyDependencyDescriptor(dep));
	    }
	}

	@Override
	public File getFile() {
		return null;
	}

	@Override
	public List<DependencyDescriptor> getDependencies() {
		return deps;
	}

	@Override
	public String getGroupId() {
		String ret = model.getGroupId();
		if(ret == null){
		    Parent parent = model.getParent();
		    if(parent != null)
		        ret = parent.getGroupId();
		}
		return ret;
	}

	@Override
	public String getArtifactId() {
		return model.getArtifactId();
	}

	@Override
	public String getClassifier() {
	    return null;
	}

	@Override
	public String getVersion() {
        String ret = model.getVersion();
        if(ret == null){
            Parent parent = model.getParent();
            if(parent != null)
                ret = parent.getVersion();
        }
        return ret;
	}

	@Override
	public boolean isOptional() {
		return false;
	}

    @Override
    public boolean isProvidedScope() {
        return false;
    }

    @Override
    public boolean isCompileScope() {
        return true;
    }

    @Override
    public boolean isRuntimeScope() {
        return false;
    }

    @Override
    public boolean isTestScope() {
        return false;
    }

    @Override
    public List<ExclusionDescriptor> getExclusions() {
        return null;
    }
}
