package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

public class ModelDependencyDescriptor implements DependencyDescriptor {

	private Model model;
    private List<DependencyDescriptor> deps;

	ModelDependencyDescriptor(Model model) {
		this.model = model;
	    deps = new ArrayList<>(model.getDependencies().size());
	    for(Dependency dep : model.getDependencies()){
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
		return model.getGroupId();
	}

	@Override
	public String getArtifactId() {
		return model.getArtifactId();
	}

	@Override
	public String getVersion() {
		return model.getVersion();
	}

	@Override
	public boolean isOptional() {
		return false;
	}

}
