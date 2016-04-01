package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.util.List;

public interface DependencyDescriptor {
	public File getFile();

	public List<DependencyDescriptor> getDependencies();

	public String getGroupId();
	public String getArtifactId();
	public String getVersion();
	public boolean isOptional();
	
}