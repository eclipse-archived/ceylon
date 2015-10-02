package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.DeclarationKind.CONSTRUCTOR;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A constructor.
 *
 * @author Gavin King
 */
public class Constructor extends TypeDeclaration implements Functional {

    private static final int ABSTRACT = 1<<13;
    private static final int OVERLOADED = 1<<19;
    private static final int ABSTRACTION = 1<<20;
    
    private ParameterList parameterList;
    private List<Declaration> overloads;
    private List<Declaration> members = new ArrayList<Declaration>(3);
    private List<Annotation> annotations = new ArrayList<Annotation>(4);
    
    public boolean isValueConstructor() {
        return parameterList==null;
    }

    @Override
    public boolean isAbstract() {
        return (flags&ABSTRACT)!=0;
    }
    
    public void setAbstract(boolean abstr) {
        if (abstr) {
            flags|=ABSTRACT;
        }
        else {
            flags&=(~ABSTRACT);
        }
    }
    
    public ParameterList getParameterList() {
        return parameterList;
    }
    
    @Override
    public ParameterList getFirstParameterList() {
        return getParameterList();
    }

    @Override
    public List<ParameterList> getParameterLists() {
        ParameterList parameterList = getParameterList();
        if (parameterList==null) {
            return emptyList();
        }
        else {
            return singletonList(parameterList);
        }
    }

    @Override
    public void addParameterList(ParameterList pl) {
        parameterList = pl;
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
    public boolean isDeclaredVoid() {
        return false;
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
    public TypeDeclaration getInheritingDeclaration(
            Declaration member) {
        if (member.getContainer().equals(this)) {
            return null;
        }
        else if (getContainer()!=null) {
            return getContainer()
                    .getInheritingDeclaration(member);
        }
        else {
            return null;
        }
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
        StringBuilder params = new StringBuilder();
        for (ParameterList pl: getParameterLists()) {
            params.append("(");
            boolean first = true;
            for (Parameter p: pl.getParameters()) {
                if (first) {
                    first = false;
                }
                else {
                    params.append(", ");
                }
                if (p.getType()!=null) {
                    params.append(p.getType().asString());
                    params.append(" ");
                }
                params.append(p.getName());
            }
            params.append(")");
        }
        return "new " + toStringName() + params;
    }
    
    @Override
    public boolean isAnonymous() {
        return true;
    }
    
    @Override
    public boolean isFinal() {
        return true;
    }
    
}
