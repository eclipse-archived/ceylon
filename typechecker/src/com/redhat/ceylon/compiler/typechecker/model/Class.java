package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isNamed;

import java.util.Collections;
import java.util.List;


public class Class extends ClassOrInterface implements Functional {
	
    boolean abstr;
    //boolean formal;    
	ParameterList parameterList;
	
	/*public boolean isFormal() {
		return formal;
	}

	public void setFormal(boolean formal) {
		this.formal = formal;
	}*/

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
        for ( Declaration d: getMembers() ) {
            if ( isParameter(d) && isNamed(name, d)) {
                return (Parameter) d;
            }
        }
        return null;
    }
    
}
