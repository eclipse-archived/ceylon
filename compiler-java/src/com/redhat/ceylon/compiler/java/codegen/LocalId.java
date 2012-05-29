package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Scope;

public interface LocalId {
    public String getLocalId(Scope d);
}
