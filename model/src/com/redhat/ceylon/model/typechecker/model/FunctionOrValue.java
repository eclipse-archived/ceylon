package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionOrValue extends TypedDeclaration {
    
    private boolean captured;
    private boolean shortcutRefinement;
    private Parameter initializerParameter;
    private List<Declaration> members = new ArrayList<Declaration>(3);
    private List<Annotation> annotations = new ArrayList<Annotation>(4);
    private List<Declaration> overloads;
    
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

    @Override
    public List<Declaration> getOverloads() {
        return overloads;
    }

    public void setOverloads(List<Declaration> overloads) {
        this.overloads = overloads;
    }

    public void initOverloads(FunctionOrValue... initial) {
        overloads = 
                new ArrayList<Declaration>
                    (initial.length+1);
        for (Declaration d: initial) {
            overloads.add(d);
        }
    }

}
