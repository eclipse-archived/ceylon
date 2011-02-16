package com.redhat.ceylon.compiler.model;


public class Class extends ClassOrInterface {
	
    Boolean isAbstract;
    Boolean formal;    
	ParameterList parameterList;
	
	public Boolean isFormal() {
		return formal;
	}

	public void setFormal(Boolean formal) {
		this.formal = formal;
	}

	public Boolean isAbstract() {
		return isAbstract;
	}	
	
	public ParameterList getParameterList() {
        return parameterList;
    }
	
	public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }
    
}
