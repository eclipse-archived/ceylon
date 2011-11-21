package com.redhat.ceylon.compiler.typechecker.exceptions;

/**
 * Unable to find the language module and hence essential types for the type checker
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class LanguageModuleNotFoundException extends RuntimeException {	
	private static final long serialVersionUID = -3520692521592355218L;
	public LanguageModuleNotFoundException(String message) {
        super(message);
    }
}
