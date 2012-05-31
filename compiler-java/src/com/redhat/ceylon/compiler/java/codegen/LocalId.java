package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Scope;

interface LocalId {
    public String getLocalId(Scope d);
}
