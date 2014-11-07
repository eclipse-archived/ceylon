package com.redhat.ceylon.compiler.typechecker.model;

import com.redhat.ceylon.compiler.typechecker.model.Util;

import java.util.Collections;
import java.util.List;


public class Class extends ClassOrInterface implements Functional {

    private boolean abstr;
    private ParameterList parameterList;
    private boolean overloaded;
    private boolean abstraction;
    private boolean anonymous;
    private boolean named = true;
    private boolean fin;
    private List<Declaration> overloads;
    private List<ProducedReference> unimplementedFormals = 
            Collections.<ProducedReference>emptyList();

    @Override
    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * Return true if we have are anonymous and have a name which is not system-generated. Currently
     * only object expressions have no name.
     */
    @Override
    public boolean isNamed() {
        return named;
    }
    
    public void setNamed(boolean named){
        this.named = named;
    }
    
    @Override
    public boolean isAbstract() {
        return abstr;
    }

    public void setAbstract(boolean isAbstract) {
        this.abstr = isAbstract;
    }

    public ParameterList getParameterList() {
        return parameterList;
    }

    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }

    @Override
    public List<ParameterList> getParameterLists() {
        if (parameterList==null) {
            return Collections.emptyList();
        }
        else {
            return Collections.singletonList(parameterList);
        }
    }

    @Override
    public void addParameterList(ParameterList pl) {
        parameterList = pl;
    }

    public Parameter getParameter(String name) {
        for (Declaration d : getMembers()) {
            if (d.isParameter() && Util.isNamed(name, d)) {
                return ((MethodOrValue)d).getInitializerParameter();
            }
        }
        return null;
    }
    
    @Override
    public boolean isOverloaded() {
    	return overloaded;
    }
    
    public void setOverloaded(boolean overloaded) {
		this.overloaded = overloaded;
	}
    
    @Override
    public Class getExtendedTypeDeclaration() {
        return (Class) super.getExtendedTypeDeclaration();
    }
    
    public void setAbstraction(boolean abstraction) {
        this.abstraction = abstraction;
    }
    
    @Override
    public boolean isAbstraction() {
        return abstraction;
    }
    
    @Override
    public boolean isFinal() {
		return fin||anonymous;
	}
    
    public void setFinal(boolean fin) {
		this.fin = fin;
	}

    @Override
    public List<Declaration> getOverloads() {
        return overloads;
    }
    
    public void setOverloads(List<Declaration> overloads) {
        this.overloads = overloads;
    }
    
    @Override
    public boolean isDeclaredVoid() {
        return false;
    }

    @Override
    public boolean isFunctional() {
        return true;
    }
    
    public List<ProducedReference> getUnimplementedFormals() {
        return unimplementedFormals;
    }
    
    public void setUnimplementedFormals(
            List<ProducedReference> unimplementedFormals) {
        this.unimplementedFormals = unimplementedFormals;
    }
}
