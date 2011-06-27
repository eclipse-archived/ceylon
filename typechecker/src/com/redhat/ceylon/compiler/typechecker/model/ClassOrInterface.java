package com.redhat.ceylon.compiler.typechecker.model;


public abstract class ClassOrInterface extends TypeDeclaration {
	
	@Override
	public boolean isMemberType() {
	    return getContainer() instanceof ClassOrInterface;
	}
	
	@Override
    public ProducedType getDeclaringType(Declaration d) {
        //look for it as a declared or inherited 
        //member of the current class or interface
        ProducedType st = getType().getSupertype((TypeDeclaration) d.getContainer());
        if (st!=null) {
            return st;
        }
        else {
            return getContainer().getDeclaringType(d);
        }
    }
	
	public abstract boolean isAbstract();

}
