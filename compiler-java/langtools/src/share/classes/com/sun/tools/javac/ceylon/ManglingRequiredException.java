package com.sun.tools.javac.ceylon;

import com.sun.tools.javac.code.Symbol;

public class ManglingRequiredException extends RuntimeException {
    public Symbol mangled;

    public ManglingRequiredException(Symbol mangled) {
        super();
        this.mangled = mangled;
    }
}
