package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NamedArgumentList extends Element implements Scope {
	
    private ParameterList parameterList;
    private List<String> argumentNames = new ArrayList<String>();
    private int id;
        
    public ParameterList getParameterList() {
        return parameterList;
    }
    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }
    
    @Override
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity) {
        Map<String, DeclarationWithProximity> result = super.getMatchingDeclarations(unit, startingWith, proximity+1);
        if (getParameterList()!=null) {
            for (Parameter p: getParameterList().getParameters()) {
                if (p.getName().startsWith(startingWith) && 
                		!getArgumentNames().contains(p.getName())) {
                    result.put(p.getName(), new DeclarationWithProximity(p, this));
                }
            }
        }
        return result;
    }
    
    public List<String> getArgumentNames() {
        return argumentNames;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NamedArgumentList) {
            NamedArgumentList that = (NamedArgumentList) obj;
            return id==that.id && 
                    getContainer().equals(that.getContainer());
        }
        else {
            return false;
        }
    }
    
}
