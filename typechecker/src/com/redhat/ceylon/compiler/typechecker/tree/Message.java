package com.redhat.ceylon.compiler.typechecker.tree;

import com.redhat.ceylon.common.Backend;

public interface Message {
    String getMessage();
    int getCode();
    int getLine();
    Backend getBackend();
}
