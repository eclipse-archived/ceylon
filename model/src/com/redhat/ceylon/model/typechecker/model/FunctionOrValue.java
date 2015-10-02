package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionOrValue extends TypedDeclaration {
    
    private static final int CAPTURED = 1;
    private static final int SHORTCUT_REFINEMENT = 1<<1;
    private static final int OVERLOADED = 1<<2;
    private static final int ABSTRACTION = 1<<3;
    private static final int IMPLEMENTED = 1<<4;
    
    private int flags;
    
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
        return (flags&SHORTCUT_REFINEMENT)!=0;
    }
    
    public void setShortcutRefinement(boolean shortcutRefinement) {
        if (shortcutRefinement) {
            flags|=SHORTCUT_REFINEMENT;
        }
        else {
            flags&=(~SHORTCUT_REFINEMENT);
        }
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
        return (flags&CAPTURED)!=0;
    }

    public void setCaptured(boolean captured) {
        if (captured) {
            flags|=CAPTURED;
        }
        else {
            flags&=(~CAPTURED);
        }
    }

    @Override
    public boolean isOverloaded() {
        return (flags&OVERLOADED)!=0;
    }
    
    public void setOverloaded(boolean overloaded) {
        if (overloaded) {
            flags|=OVERLOADED;
        }
        else {
            flags&=(~OVERLOADED);
        }
    }
    
    public void setAbstraction(boolean abstraction) {
        if (abstraction) {
            flags|=ABSTRACTION;
        }
        else {
            flags&=(~ABSTRACTION);
        }
    }
    
    @Override
    public boolean isAbstraction() {
        return (flags&ABSTRACTION)!=0;
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
    
    public boolean isImplemented() {
        return (flags&IMPLEMENTED)!=0;
    }
    
    public void setImplemented(boolean implemented) {
        if (implemented) {
            flags|=IMPLEMENTED;
        }
        else {
            flags&=(~IMPLEMENTED);
        }
    }

}
