package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.aether.graph.DependencyNode;

public class DependencyNodeDependencyDescriptor implements DependencyDescriptor {
	private DependencyNode node;
	private List<DependencyDescriptor> deps;
	
	DependencyNodeDependencyDescriptor(DependencyNode node){
	    this.node = node;
	    deps = new ArrayList<>(node.getChildren().size());
	    for(DependencyNode dep : node.getChildren()){
	        deps.add(new DependencyNodeDependencyDescriptor(dep));
	    }
	}

	@Override
	public File getFile() {
		return node.getArtifact().getFile();
	}

	@Override
	public List<DependencyDescriptor> getDependencies() {
		return deps;
	}

	@Override
	public String getGroupId() {
		return node.getArtifact().getGroupId();
	}

	@Override
	public String getArtifactId() {
		return node.getArtifact().getArtifactId();
	}

	@Override
	public String getVersion() {
		return node.getArtifact().getVersion();
	}

	@Override
	public boolean isOptional() {
		return node.getDependency().isOptional();
	}
}