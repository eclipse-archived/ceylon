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
    public Map<String, Declaration> getMatchingDeclarations(Unit unit, String startingWith) {
        Map<String, Declaration> result = super.getMatchingDeclarations(unit, startingWith);
        for (Parameter p: getParameterList().getParameters()) {
            if (p.getName().startsWith(startingWith)) {
                result.put(p.getName(), p);
            }
        }
        return result;
    }
    
}
