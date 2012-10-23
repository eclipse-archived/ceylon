package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.common.tool.NonFatal;

@NonFatal
public class CeylonRunJsException extends RuntimeException {

    public CeylonRunJsException(String message) {
        super(message);
    }
}
