package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.common.tool.NonFatal;

@NonFatal
public class CeylonRunJsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CeylonRunJsException(String message) {
        super(message);
    }
}
