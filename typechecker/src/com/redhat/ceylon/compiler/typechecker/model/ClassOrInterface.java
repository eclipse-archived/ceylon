package com.redhat.ceylon.compiler.typechecker.model;


public abstract class ClassOrInterface extends TypeDeclaration {

    @Override
    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface;
    }

    @Override
    public ProducedType getDeclaringType(Declaration d) {
        //look for it as a declared or inherited 
        //member of the current class or interface
    	if (d.isMember()) {
	        ProducedType st = getType().getSupertype((TypeDeclaration) d.getContainer());
	        //return st;
	        if (st!=null) {
	            return st;
	        }
	        else {
	            return getContainer().getDeclaringType(d);
	        }
    	}
    	else {
    		return null;
    	}
    }

    public abstract boolean isAbstract();

    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.TYPE;
    }
    
}
