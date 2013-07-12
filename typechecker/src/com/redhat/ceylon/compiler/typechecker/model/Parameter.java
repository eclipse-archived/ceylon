package com.redhat.ceylon.compiler.typechecker.model;


public abstract class Parameter extends TypedDeclaration {
    
	private boolean defaulted;
	private boolean sequenced;
    private Declaration declaration;
    private boolean captured = false;
    private Parameter aliasedParameter = this;
    private boolean atLeastOne = false;
    
    @Override
    public boolean isCaptured() {
        return captured;
    }
    
    public void setCaptured(boolean local) {
        this.captured = local;
    }
    
    public boolean isDefaulted() {
        return defaulted;
    }
    
    public void setDefaulted(boolean defaulted) {
        this.defaulted = defaulted;
    }
    
    public boolean isSequenced() {
        return sequenced;
    }
    
    public void setSequenced(boolean sequenced) {
        this.sequenced = sequenced;
    }
    
    public boolean isAtLeastOne() {
        return atLeastOne;
    }
    
    public void setAtLeastOne(boolean atLeastOne) {
        this.atLeastOne = atLeastOne;
    }
    
    public Declaration getDeclaration() {
        return declaration;
    }
    
    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }
    
    public Parameter getAliasedParameter() {
        return aliasedParameter;
    }
    
    public void setAliasedParameter(Parameter parameter) {
        this.aliasedParameter = parameter;
    }
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.PARAMETER;
    }

    @Override
    public String toString() {
        return super.toString().replaceFirst("\\[", 
        		"[" + declaration.getName() + "#");
    }
    
}
