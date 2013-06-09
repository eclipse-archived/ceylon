package com.redhat.ceylon.compiler.typechecker.model;

public class TypeAlias extends TypeDeclaration {

	@Override
	public DeclarationKind getDeclarationKind() {
		return DeclarationKind.TYPE;
	}
	
	@Override
	public boolean isAlias() {
		return true;
	}
	
    @Override
    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface;
    }

}
