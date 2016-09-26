package com.redhat.ceylon.cmr.resolver.aether;

import org.eclipse.aether.RepositoryException;

@SuppressWarnings("serial")
public class AetherException extends Exception {
	AetherException(RepositoryException cause){
		super(cause);
	}
}