package com.redhat.ceylon.cmr.resolver.aether;

import com.redhat.ceylon.aether.eclipse.aether.RepositoryException;

@SuppressWarnings("serial")
public class AetherException extends Exception {
	AetherException(RepositoryException cause){
		super(cause);
	}
}