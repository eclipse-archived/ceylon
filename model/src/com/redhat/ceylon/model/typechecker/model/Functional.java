package com.redhat.ceylon.model.typechecker.model;

import java.util.List;

/**
 * Represents a declaration which has a list of parameters.
 *
 * @author Gavin King
 */
public interface Functional extends Typed {

    public ParameterList getFirstParameterList();
    
    public List<ParameterList> getParameterLists();

    public void addParameterList(ParameterList pl);

    public String getName();
    public String getName(Unit unit);
    
    public Parameter getParameter(String name);

//    public List<TypeParameter> getTypeParameters();
    
    public boolean isDeclaredVoid();

}
