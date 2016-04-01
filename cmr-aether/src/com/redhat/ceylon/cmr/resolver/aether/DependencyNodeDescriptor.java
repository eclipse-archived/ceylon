package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.aether.graph.DependencyNode;

public class DependencyNodeDescriptor implements DependencyDescriptor {
	private DependencyNode node;

	DependencyNodeDescriptor(DependencyNode node){
		this.node = node;
	}

	@Override
	public File getFile() {
		return node.getArtifact().getFile();
	}

	@Override
	public List<DependencyDescriptor> getDependencies() {
		List<DependencyDescriptor> ret = new ArrayList<>(node.getChildren().size());
		for(DependencyNode dep : node.getChildren()){
			ret.add(new DependencyNodeDescriptor(dep));
		}
		return ret;
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