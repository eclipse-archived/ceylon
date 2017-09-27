package org.eclipse.ceylon.model.loader.model;

import org.eclipse.ceylon.model.typechecker.model.Declaration;

public interface LazyContainer extends LazyElement, LocalDeclarationContainer {
    public void addMember(Declaration decl);

}
