package com.redhat.ceylon.model.loader.model;

import com.redhat.ceylon.model.typechecker.model.Declaration;

public interface LazyContainer extends LazyElement, LocalDeclarationContainer {
    public void addMember(Declaration decl);

}
