package com.sun.tools.javac.ceylon;

import com.sun.tools.javac.code.Symbol.MethodSymbol;

public class ExtensionRequiredException extends RuntimeException {
    public MethodSymbol extension;

    public ExtensionRequiredException(MethodSymbol extension) {
        super();
        this.extension = extension;
    }
}
