package com.redhat.ceylon.compiler.typechecker.exceptions;

/**
 * Unable to find the language module and hence essential types for the type checker
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class LanguageModuleNotFoundException extends RuntimeException {
    public LanguageModuleNotFoundException(String message) {
        super(message);
    }
}
