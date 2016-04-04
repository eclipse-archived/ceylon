package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface AetherResolver {
    
	public DependencyDescriptor getDependencies(String groupId, String artifactId, String version, boolean fetchSingleArtifact) 
    		throws AetherException;
    
    public DependencyDescriptor getDependencies(String groupId, String artifactId, String version, 
    		String classifier, String extension, boolean fetchSingleArtifact) 
    				throws AetherException;
    
    public List<String> resolveVersionRange(String groupId, String artifactId, String versionRange) throws AetherException;

    public DependencyDescriptor getDependencies(File pomXml, String name, String version) throws IOException;

    public DependencyDescriptor getDependencies(InputStream pomXml, String name, String version) throws IOException;
}
