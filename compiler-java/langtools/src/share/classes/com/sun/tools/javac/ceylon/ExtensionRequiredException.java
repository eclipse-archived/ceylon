package com.sun.tools.javac.ceylon;

import com.sun.tools.javac.ceylon.ExtensionFinder.Route;

public class ExtensionRequiredException extends RuntimeException {
    public Route extension;

    public ExtensionRequiredException(Route extension) {
        super();
        this.extension = extension;
    }
}
