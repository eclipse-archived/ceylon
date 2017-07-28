package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.util.List;

import com.redhat.ceylon.aether.apache.maven.model.Dependency;
import com.redhat.ceylon.aether.apache.maven.model.Exclusion;
import com.redhat.ceylon.aether.eclipse.aether.util.artifact.JavaScopes;

public class DependencyDependencyDescriptor implements DependencyDescriptor {

	private Dependency model;
    private List<ExclusionDescriptor> exclusions;

	DependencyDependencyDescriptor(Dependency model) {
		this.model = model;
		for(Exclusion x : model.getExclusions()){
		    exclusions.add(new ExclusionExclusionDescriptor(x));
		}
	}

	@Override
	public File getFile() {
		return null;
	}

	@Override
	public List<DependencyDescriptor> getDependencies() {
		return null;
	}

	@Override
	public String getGroupId() {
		return model.getGroupId();
	}

	@Override
	public String getArtifactId() {
		return model.getArtifactId();
	}
	
	@Override
	public String getClassifier() {
	    return model.getClassifier();
	}

	@Override
	public String getVersion() {
		return model.getVersion();
	}

	@Override
	public boolean isOptional() {
		return model.isOptional();
	}

    @Override
    public boolean isProvidedScope() {
        return JavaScopes.PROVIDED.equals(model.getScope());
    }

    @Override
    public boolean isRuntimeScope() {
        return JavaScopes.RUNTIME.equals(model.getScope());
    }
    
    @Override
    public boolean isCompileScope() {
        return JavaScopes.COMPILE.equals(model.getScope());
    }
    
    @Override
    public boolean isTestScope() {
        return JavaScopes.TEST.equals(model.getScope());
    }

    @Override
    public List<ExclusionDescriptor> getExclusions() {
        return exclusions;
    }

}
