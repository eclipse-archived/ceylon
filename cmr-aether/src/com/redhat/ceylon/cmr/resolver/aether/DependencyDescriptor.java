package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.util.List;

public interface DependencyDescriptor {
	public File getFile();

	public List<DependencyDescriptor> getDependencies();
	public List<ExclusionDescriptor> getExclusions();

	public String getGroupId();
	public String getArtifactId();
	public String getClassifier();
	public String getVersion();
	public boolean isOptional();
	
    public boolean isProvidedScope();
    public boolean isCompileScope();
    public boolean isRuntimeScope();
    public boolean isTestScope();
}