package com.redhat.ceylon.compiler.typechecker.model;

public abstract class MethodOrValue extends TypedDeclaration {
    
    private boolean shortcutRefinement;
    
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

}
