package com.redhat.ceylon.compiler.model;

import java.util.Collections;
import java.util.List;


public class Class extends ClassOrInterface implements Functional {
	
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
	
	@Override
	public ProducedType getType() {
	    ProducedType pt = new ProducedType();
	    pt.setDeclaration(this);
	    for (TypeDeclaration t: getTypeParameters()) {
	        ProducedType pta = new ProducedType();
	        pta.setDeclaration(t);
	        pt.getTypeArguments().add(pta);
	    }
	    return pt;
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
    
}
