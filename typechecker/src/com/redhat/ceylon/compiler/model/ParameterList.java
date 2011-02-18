package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class ParameterList extends Model {
    
    private List<Parameter> parameters = new ArrayList<Parameter>();
    
    public List<Parameter> getParameters() {
        return parameters;
    }
    
    @Override
    public String toString() {
        return "ParameterList" + parameters.toString();
    }
    
}
