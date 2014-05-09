package com.redhat.ceylon.compiler.typechecker.model;


public class UnknownType extends TypeDeclaration {

    private Runnable errorReporter;
    
    @Override
    public void addMember(Declaration declaration) {
        throw new UnsupportedOperationException();
    }
    
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
    
    @Override
    public boolean equals(Object object) {
    	return this==object;
    }
    
    @Override
    public int hashCode() {
    	return System.identityHashCode(this);
    }

    public void setErrorReporter(Runnable errorReporter) {
        this.errorReporter = errorReporter;
    }
    
    public Runnable getErrorReporter(){
        return errorReporter;
    }

    public void reportErrors() {
        if(errorReporter != null){
            errorReporter.run();
        }
    }

    @Override
    protected int hashCodeForCache() {
        return hashCode();
    }
    
    @Override
    protected boolean equalsForCache(Object o) {
        return equals(o);
    }
}
