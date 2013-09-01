package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public class TypeAlias extends TypeDeclaration {

    private List<Declaration> members = new ArrayList<Declaration>(3);
    private List<Annotation> annotations = new ArrayList<Annotation>(4);
    
    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }
    
    @Override
    public List<Declaration> getMembers() {
        return members;
    }
    
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
