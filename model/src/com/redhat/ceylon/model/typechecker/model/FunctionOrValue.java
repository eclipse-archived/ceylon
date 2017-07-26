package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.DeclarationFlags.FunctionOrValueFlags.*;
import java.util.ArrayList;
import java.util.List;

public abstract class FunctionOrValue extends TypedDeclaration {
    
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
    public boolean isJsCaptured() {
        return (flags&JS_CAPTURED)!=0;
    }

    public void setJsCaptured(boolean captured) {
        if (captured) {
            flags|=JS_CAPTURED;
        }
        else {
            flags&=(~JS_CAPTURED);
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

    public void setSmall(boolean small) {
        if (small) {
            flags|=SMALL;
        }
        else {
            flags&=(~SMALL);
        }
    }
    
    public boolean isSmall() {
        return (flags&SMALL)!=0;
    }

    public void setJavaNative(boolean b) {
        if (b) {
            flags|=JAVA_NATIVE;
        }
        else {
            flags&=(~JAVA_NATIVE);
        }
    }
    
    @Override
    public boolean isJavaNative() {
        return (flags&JAVA_NATIVE)!=0;
    }

}
