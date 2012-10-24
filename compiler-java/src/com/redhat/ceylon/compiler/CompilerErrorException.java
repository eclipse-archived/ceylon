package com.redhat.ceylon.compiler;

import com.redhat.ceylon.common.tool.NonFatal;

@NonFatal
public class CompilerErrorException extends RuntimeException {

    public CompilerErrorException(String message) {
        super(message);
    }
}
