package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.common.tool.NonFatal;

@NonFatal
public class CompilerErrorException extends RuntimeException {

    public CompilerErrorException(String message) {
        super(message);
    }
}
