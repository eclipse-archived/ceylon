package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public class ParameterList {
    
    private List<Parameter> parameters = new ArrayList<Parameter>();
    
    public List<Parameter> getParameters() {
        return parameters;
    }
    
    @Override
    public String toString() {
        return "ParameterList" + parameters.toString();
    }
    
}
