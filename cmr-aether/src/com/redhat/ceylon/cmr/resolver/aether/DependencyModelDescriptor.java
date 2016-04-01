package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.util.List;

import org.apache.maven.model.Dependency;

public class DependencyModelDescriptor implements DependencyDescriptor {

	private Dependency model;

	DependencyModelDescriptor(Dependency model) {
		this.model = model;
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
	public String getVersion() {
		return model.getVersion();
	}

	@Override
	public boolean isOptional() {
		return model.isOptional();
	}

}
