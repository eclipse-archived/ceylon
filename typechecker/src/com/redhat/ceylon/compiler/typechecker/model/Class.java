package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Collections;
import java.util.List;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isNamed;


public class Class extends ClassOrInterface implements Functional {

    private boolean abstr;
    //private boolean formal;    
    private ParameterList parameterList;
    private boolean overloaded;

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
        for (Declaration d : getMembers()) {
            if (isParameter(d) && isNamed(name, d)) {
                return (Parameter) d;
            }
        }
        return null;
    }
    
    @Override
    public boolean isOverloaded() {
    	return overloaded;
    }
    
    @Override
    public Class getExtendedTypeDeclaration() {
        return (Class) super.getExtendedTypeDeclaration();
    }

}
