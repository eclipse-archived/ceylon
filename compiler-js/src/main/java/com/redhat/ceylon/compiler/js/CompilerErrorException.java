package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.common.tool.NonFatal;

@NonFatal
public class CompilerErrorException extends RuntimeException {

    private static final long serialVersionUID = 5L;

    public CompilerErrorException(String message) {
        super(message);
    }
}
