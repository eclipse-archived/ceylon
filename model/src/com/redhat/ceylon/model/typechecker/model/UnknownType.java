package com.redhat.ceylon.model.typechecker.model;


public class UnknownType extends TypeDeclaration {

    private ErrorReporter errorReporter;
    
    public static class ErrorReporter {
        private String message;
        
        public ErrorReporter(String message){
            this.message = message;
        }
        
        public String getMessage(){
            return message;
        }
        
        public void reportError(){}
    }
    
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
        return getName();
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

    public void setErrorReporter(ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }
    
    public ErrorReporter getErrorReporter(){
        return errorReporter;
    }

    public void reportErrors() {
        if(errorReporter != null){
            errorReporter.reportError();
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
