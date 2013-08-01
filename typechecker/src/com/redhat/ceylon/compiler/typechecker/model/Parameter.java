package com.redhat.ceylon.compiler.typechecker.model;


public class Parameter {
    
	private boolean defaulted;
	private boolean sequenced;
    private Declaration declaration;
//    private boolean captured = false;
    private Parameter aliasedParameter = this;
    private boolean atLeastOne = false;
    private boolean declaredAnything = false;
    private MethodOrValue model;
    private String name;
    
    private boolean hidden;
    
    /**
     * Is this a split-style parameter declaration where
     * just the name of the parameter appears in the
     * parameter list?
     */
    public boolean isHidden() {
        return hidden;
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    
    public MethodOrValue getModel() {
        return model;
    }
    public void setModel(MethodOrValue model) {
        this.model = model;
    }
    
//    @Override
//    public boolean isCaptured() {
//        return captured;
//    }
//    
//    public void setCaptured(boolean local) {
//        this.captured = local;
//    }
    
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
    
    public boolean isDeclaredAnything() {
        return declaredAnything;
    }
    
    public void setDeclaredAnything(boolean declaredAnything) {
        this.declaredAnything = declaredAnything;
    }
    
    public boolean isDeclaredVoid() {
        return model instanceof Method && 
                ((Method) model).isDeclaredVoid();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ProducedType getType() {
        return model==null ? null : model.getType();
    }
    
    /*@Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.PARAMETER;
    }*/

    @Override
    public String toString() {
        return model.toString().replaceFirst("\\[", 
        		"[" + declaration.getName() + "#");
    }
    
}
