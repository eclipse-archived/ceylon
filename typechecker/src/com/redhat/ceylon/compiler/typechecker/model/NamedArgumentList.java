package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NamedArgumentList extends Element implements Scope {
	
    private ParameterList parameterList;
    private List<String> argumentNames = new ArrayList<String>();
    
    public ParameterList getParameterList() {
        return parameterList;
    }
    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }
    
    @Override
    public Map<DeclarationKey, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity) {
        Map<DeclarationKey, DeclarationWithProximity> result = super.getMatchingDeclarations(unit, startingWith, proximity+1);
        if (getParameterList()!=null) {
            for (Parameter p: getParameterList().getParameters()) {
                if (p.getName().startsWith(startingWith) && !getArgumentNames().contains(p.getName())) {
                    result.put(new DeclarationKey(p), new DeclarationWithProximity(p, proximity));
                }
            }
        }
        return result;
    }
    
    public List<String> getArgumentNames() {
        return argumentNames;
    }
    
}
