package com.redhat.ceylon.model.typechecker.model;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A function. Note that a function must have at least one 
 * parameter list.
 *
 * @author Gavin King
 */
public class Function extends FunctionOrValue implements Generic, Scope, Functional {

    private static final int VOID = 1<<22;
    private static final int DEFERRED = 1<<23;
    private static final int ANONYMOUS = 1<<24;
    
    private List<TypeParameter> typeParameters = emptyList();
    private List<ParameterList> parameterLists = new ArrayList<ParameterList>(1);
    private Object annotationConstructor;
    
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
    public ParameterList getFirstParameterList() {
        return getParameterLists().get(0);
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
    public boolean isDeclaredVoid() {
        return (flags&VOID)!=0;
    }
    
    public void setDeclaredVoid(boolean declaredVoid) {
        if (declaredVoid) {
            flags|=VOID;
        }
        else {
            flags&=(~VOID);
        }
    }
    
    public boolean isDeferred() {
        return (flags&DEFERRED)!=0;
    }
    
    public void setDeferred(boolean deferred) {
        if (deferred) {
            flags|=DEFERRED;
        }
        else {
            flags&=(~DEFERRED);
        }
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
        if (anonymous) {
            flags|=ANONYMOUS;
        }
        else {
            flags&=(~ANONYMOUS);
        }
    }
    
    @Override
    public boolean isAnonymous() {
        return (flags&ANONYMOUS)!=0;
    }
    
    /**
     * Returns true if this method is anonymous.
     */
    @Override
    public boolean isNamed() {
        return (flags&ANONYMOUS)==0;
    }

    @Override
    public Set<String> getScopedBackends() {
        return super.getScopedBackends();
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
