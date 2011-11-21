package com.redhat.ceylon.compiler.typechecker.exceptions;

/**
 * Unable to find the language module and hence essential types for the type checker
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class LanguageModuleNotfoundException extends RuntimeException {
    public LanguageModuleNotfoundException(String message) {
        super(message);
    }
}
