package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;

public interface LazyElement {
    public void addMember(Declaration decl);
}
