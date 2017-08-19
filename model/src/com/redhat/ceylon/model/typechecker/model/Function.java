package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.DeclarationFlags.FunctionFlags.DEFERRED;
import static com.redhat.ceylon.model.typechecker.model.DeclarationFlags.FunctionFlags.NO_NAME;
import static com.redhat.ceylon.model.typechecker.model.DeclarationFlags.FunctionFlags.VOID;
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

    private List<TypeParameter> typeParameters = emptyList();
    private List<ParameterList> parameterLists = new ArrayList<ParameterList>(1);
    private Object annotationConstructor;
    private Function realFunction;
    
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
        List<ParameterList> lists = getParameterLists();
        return lists.isEmpty() ? null : lists.get(0);
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

    //TODO: replace with setNamed()
    public void setAnonymous(boolean anonymous) {
        if (anonymous) {
            flags|=NO_NAME;
        }
        else {
            flags&=(~NO_NAME);
        }
    }
    
    @Override
    public boolean isAnonymous() {
        return (flags&NO_NAME)!=0;
    }
    
    /**
     * Returns true if this method is anonymous.
     */
    @Override
    public boolean isNamed() {
        return (flags&NO_NAME)==0;
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
                FunctionOrValue model = p.getModel();
                if (model!=null 
                        && model.getType()!=null) {
                    Type type;
                    if (model.isFunctional()) {
                        type = model.getTypedReference()
                                .getFullType();
                    }
                    else {
                        type = model.getType();
                    }
                    params.append(type.asString())
                          .append(" ");
                }
                params.append(p.getName());
            }
            params.append(")");
        }
        Type type = getType();
        return "function " + toStringName() + params + 
                (type==null ? "" : " => " + type.asString());
    }

    public Function getRealFunction() {
        return realFunction;
    }

    public void setRealFunction(Function realFunction) {
        this.realFunction = realFunction;
    }
    
}
