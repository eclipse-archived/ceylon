package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.DeclarationKind.CONSTRUCTOR;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A method. Note that a method must have
 * at least one parameter list.
 *
 * @author Gavin King
 */
public class Constructor extends TypeDeclaration implements Generic, Scope, Functional {

    public Constructor() {}
    
    //private boolean formal;

    private List<TypeParameter> typeParameters = emptyList();
    private List<ParameterList> parameterLists = new ArrayList<ParameterList>(1);
    private boolean overloaded;
    private boolean abstraction;
    private boolean abstr;
    private List<Declaration> overloads;
    private List<Declaration> members = new ArrayList<Declaration>(3);
    private List<Annotation> annotations = new ArrayList<Annotation>(4);
    
    @Override
    public boolean isAbstract() {
        return abstr;
    }
    
    public void setAbstract(boolean isAbstract) {
        this.abstr = isAbstract;
    }

    @Override
    public boolean isParameterized() {
        return !typeParameters.isEmpty();
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }

    @Override
    public List<ParameterList> getParameterLists() {
        return parameterLists;
    }

    @Override
    public void addParameterList(ParameterList pl) {
        parameterLists.add(pl);
    }

    @Override
    public boolean isOverloaded() {
    	return overloaded;
    }
    
    public void setOverloaded(boolean overloaded) {
		this.overloaded = overloaded;
	}
    
    public void setAbstraction(boolean abstraction) {
        this.abstraction = abstraction;
    }
    
    @Override
    public boolean isAbstraction() {
        return abstraction;
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
        
}
