package com.redhat.ceylon.compiler.typechecker.model;

public abstract class MethodOrValue extends TypedDeclaration {
    
    private boolean captured;
    private boolean shortcutRefinement;
    private ValueParameter initializerParameter;
    
    public boolean isShortcutRefinement() {
        return shortcutRefinement;
    }
    
    public void setShortcutRefinement(boolean shortcutRefinement) {
        this.shortcutRefinement = shortcutRefinement;
    }
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.MEMBER;
    }
    
    public ValueParameter getInitializerParameter() {
        return initializerParameter;
    }

    public void setInitializerParameter(ValueParameter d) {
        initializerParameter = d;
    }

    public boolean isTransient() {
        return true;
    }

    @Override
    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean local) {
        this.captured = local;
    }

    public abstract void setSetter(Setter setter);

}
