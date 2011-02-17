package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A functional parameter, i.e. a parameter that
 * declares its own parameter list.
 * 
 * @author Gavin King
 *
 */
public class FunctionalParameter extends Parameter implements Functional {
    
    List<ParameterList> parameterLists = new ArrayList<ParameterList>();
    
    @Override
    public List<ParameterList> getParameterLists() {
        return parameterLists;
    }
    
    @Override
    public void addParameterList(ParameterList pl) {
        parameterLists.add(pl);
    }
    
}
