package com.redhat.ceylon.model.typechecker.model;

import java.util.List;

/**
 * Represents a declaration which has a list of parameters.
 *
 * @author Gavin King
 */
public interface Functional extends Typed, Named {

    public ParameterList getFirstParameterList();
    
    public List<ParameterList> getParameterLists();

    public void addParameterList(ParameterList pl);

    public Parameter getParameter(String name);

//    public List<TypeParameter> getTypeParameters();
    
    public boolean isDeclaredVoid();

}
