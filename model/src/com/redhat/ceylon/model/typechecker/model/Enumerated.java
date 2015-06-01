package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.DeclarationKind.CONSTRUCTOR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public final class Enumerated extends TypeDeclaration {
    
    private List<Declaration> members = new ArrayList<Declaration>(3);
    private List<Annotation> annotations = new ArrayList<Annotation>(4);
    
    @Override
    public boolean isConstructor() {
        return true;
    }
    
    @Override
    public boolean isFinal() {
        return true;
    }
    
    @Override
    public boolean isAnonymous() {
        return true;
    }
    
    @Override
    public Type getDeclaringType(Declaration d) {
        if (d.isMember()) {
            return getContainer().getDeclaringType(d);
        }
        else {
            return null;
        }
    }
    
    public Parameter getParameter(String name) {
        for (Declaration d : getMembers()) {
            if (d.isParameter() && ModelUtil.isNamed(name, d)) {
                return ((FunctionOrValue) d).getInitializerParameter();
            }
        }
        return null;
    }
    
    @Override
    public boolean isFunctional() {
        return true;
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }
    
    @Override
    public List<Declaration> getMembers() {
        return members;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        members.add(declaration);
    }
    
    @Override
    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface;
    }

    @Override
    public DeclarationKind getDeclarationKind() {
        return CONSTRUCTOR;
    }
    
    protected Declaration getMemberOrParameter(String name, 
            List<Type> signature, boolean ellipsis) {
        return getDirectMember(name, signature, ellipsis);
    }
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        Type et = getExtendedType();
        if (et!=null) { 
            et.getDeclaration()
                .collectSupertypeDeclarations(results);
        }
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        Type et = getExtendedType();
        if (et!=null) { 
            return et.getDeclaration().inherits(dec);
        }
        else {
            return false;
        }
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        Scope container = getContainer();
        if (container instanceof Declaration) {
            ret = (37 * ret) + ((Declaration) container).hashCodeForCache();
        }
        else {
            ret = (37 * ret) + container.hashCode();
        }
        String qualifier = getQualifier();
        ret = (37 * ret) + (qualifier == null ? 0 : qualifier.hashCode());
        ret = (37 * ret) + Objects.hashCode(getName());
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if (o == null || o instanceof Constructor == false) {
            return false;
        }
        Constructor b = (Constructor) o;
        Scope container = getContainer();
        if (container instanceof Declaration) {
            if (!((Declaration) container).equalsForCache(b.getContainer())) {
                return false;
            }
        }
        else {
            if (!container.equals(b.getContainer())) {
                return false;
            }
        }
        if (!Objects.equals(getQualifier(), b.getQualifier())) {
            return false;
        }
        return Objects.equals(getName(), b.getName());
    }
    
    @Override
    public String toString() {
        return "new " + toStringName();
    }
    
}