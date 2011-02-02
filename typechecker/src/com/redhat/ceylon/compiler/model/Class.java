package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Class extends ClassOrInterface {
	
    Boolean isAbstract;
    Boolean formal;    
	List<Typed> parameters = new ArrayList<Typed>();
	
	public Boolean isFormal() {
		return formal;
	}

	public void setFormal(Boolean formal) {
		this.formal = formal;
	}

	public Boolean isAbstract() {
		return isAbstract;
	}	
	
    public List<Typed> parameters() {
    	return parameters;
    }
}
