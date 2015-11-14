package com.redhat.ceylon.model.typechecker.model;


public class Parameter {
    
	private boolean defaulted;
	private boolean sequenced;
    private Declaration declaration;
    private boolean atLeastOne = false;
    private boolean declaredAnything = false;
    private FunctionOrValue model;
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
    
    public FunctionOrValue getModel() {
        return model;
    }
    public void setModel(FunctionOrValue model) {
        this.model = model;
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
    
    public boolean isDeclaredAnything() {
        return declaredAnything;
    }
    
    public void setDeclaredAnything(boolean declaredAnything) {
        this.declaredAnything = declaredAnything;
    }
    
    public boolean isDeclaredVoid() {
        return model instanceof Function && 
                ((Function) model).isDeclaredVoid();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Type getType() {
        return model==null ? null : model.getType();
    }
    
    @Override
    public String toString() {
        return model==null ? name : model.toString();
    }
    
}
