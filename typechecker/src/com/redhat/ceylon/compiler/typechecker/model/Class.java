package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isNamed;

import java.util.Collections;
import java.util.List;


public class Class extends ClassOrInterface implements Functional {

    private boolean abstr;
    private ParameterList parameterList;
    private boolean overloaded;
    private boolean abstraction;
    private boolean anonymous;
    private boolean fin;
    private List<Declaration> overloads;

    @Override
    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
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
            if (d.isParameter() && isNamed(name, d)) {
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
    
}
