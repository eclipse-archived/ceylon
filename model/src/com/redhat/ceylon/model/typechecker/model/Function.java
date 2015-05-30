package com.redhat.ceylon.model.typechecker.model;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

/**
 * A function. Note that a function must have at least one 
 * parameter list.
 *
 * @author Gavin King
 */
public class Function extends FunctionOrValue implements Generic, Scope, Functional {

    public Function() {}
    
    //private boolean formal;

    private List<TypeParameter> typeParameters = emptyList();
    private List<ParameterList> parameterLists = new ArrayList<ParameterList>(1);
    private boolean overloaded;
    private boolean abstraction;
    private boolean declaredVoid;
    private Object annotationConstructor;
    private boolean deferred;
    private boolean anonymous;
    
    public Object getAnnotationConstructor() {
        return annotationConstructor;
    }

    public void setAnnotationConstructor(Object annotationInstantiation) {
        this.annotationConstructor = annotationInstantiation;
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
    public boolean isDeclaredVoid() {
        return declaredVoid;
    }
    
    public void setDeclaredVoid(boolean declaredVoid) {
        this.declaredVoid = declaredVoid;
    }
    
    public boolean isDeferred() {
        return deferred;
    }
    
    public void setDeferred(boolean deferred) {
        this.deferred = deferred;
    }
    
    public Parameter getParameter(String name) {
        for (Declaration d : getMembers()) {
            if (d.isParameter() && ModelUtil.isNamed(name, d)) {
                FunctionOrValue mod = (FunctionOrValue) d;
                return mod.getInitializerParameter();
            }
        }
        return null;
    }
    
    @Override
    public boolean isFunctional() {
        return true;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
    
    @Override
    public boolean isAnonymous() {
        return anonymous;
    }
    
    /**
     * Returns true if this method is anonymous.
     */
    @Override
    public boolean isNamed() {
        return !anonymous;
    }

    @Override
    public String toString() {
        Type type = getType();
        if (type==null) {
            return "function " + toStringName();
        }
        else {
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
            return "function " + toStringName() + params + 
                    " => " + type.asString();
        }
    }
    
}
