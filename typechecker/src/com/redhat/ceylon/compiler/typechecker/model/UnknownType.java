package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Arrays;
import java.util.List;

public class UnknownType extends TypeDeclaration {

    public UnknownType(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public String getName() {
        return "unknown";
    }
    
	@Override
	public DeclarationKind getDeclarationKind() {
		return DeclarationKind.TYPE;
	}

    @Override @Deprecated
    public List<String> getQualifiedName() {
        return Arrays.asList(getQualifiedNameString());
    }

    @Override
    public String getQualifiedNameString() {
        return getName();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    @Override
    public boolean isShared() {
        return true;
    }
    
}
