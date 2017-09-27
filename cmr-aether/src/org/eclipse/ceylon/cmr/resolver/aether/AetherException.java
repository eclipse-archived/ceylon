package org.eclipse.ceylon.cmr.resolver.aether;

import org.eclipse.ceylon.aether.eclipse.aether.RepositoryException;

@SuppressWarnings("serial")
public class AetherException extends Exception {
	AetherException(RepositoryException cause){
		super(cause);
	}
}