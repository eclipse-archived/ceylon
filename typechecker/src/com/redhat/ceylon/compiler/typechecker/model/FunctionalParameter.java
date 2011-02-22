package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A functional parameter, i.e. a parameter that
 * declares its own parameter list.
 * 
 * @author Gavin King
 *
 */
public class FunctionalParameter extends Parameter implements Scope, Functional {
    
    List<ParameterList> parameterLists = new ArrayList<ParameterList>();
    List<Declaration> members = new ArrayList<Declaration>(); //only for parameters!
    
    @Override
    public List<ParameterList> getParameterLists() {
        return parameterLists;
    }
    
    @Override
    public void addParameterList(ParameterList pl) {
        parameterLists.add(pl);
    }
    
    @Override
    public List<Declaration> getMembers() {
        return members;
    }
    
}
