package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Map;

public class NamedArgumentList extends Element implements Scope {
    private ParameterList parameterList;
    
    public ParameterList getParameterList() {
        return parameterList;
    }
    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }
    
    @Override
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity) {
        Map<String, DeclarationWithProximity> result = super.getMatchingDeclarations(unit, startingWith, proximity+1);
        for (Parameter p: getParameterList().getParameters()) {
            if (p.getName().startsWith(startingWith)) {
                result.put(p.getName(), new DeclarationWithProximity(p, proximity));
            }
        }
        return result;
    }
    
}
