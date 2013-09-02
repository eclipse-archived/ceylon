package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public abstract class MethodOrValue extends TypedDeclaration {
    
    private boolean captured;
    private boolean shortcutRefinement;
    private Parameter initializerParameter;
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
    
    public void addMember(Declaration declaration) {
        members.add(declaration);
    }
    
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
    
    public Parameter getInitializerParameter() {
        return initializerParameter;
    }

    public void setInitializerParameter(Parameter d) {
        initializerParameter = d;
    }
    
    @Override
    public boolean isParameter() {
        return initializerParameter!=null;
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
