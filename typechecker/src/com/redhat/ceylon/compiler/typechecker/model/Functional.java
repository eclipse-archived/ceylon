package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

/**
 * Represents a construct which may have a
 * list of parameters or more than one:
 * a formal parameter or method. We don't
 * include classes here because they can
 * have only one list of parameters.
 * <p/>
 * TODO: do we need a type to abstract
 * classes, parameters, and methods?
 *
 * @author Gavin King
 */
public interface Functional {

    public List<ParameterList> getParameterLists();

    public void addParameterList(ParameterList pl);

    public ProducedType getType();

    public String getName();
    public String getName(Unit unit);
    
    public Parameter getParameter(String name);

    public List<TypeParameter> getTypeParameters();
    
    public boolean isOverloaded();
    
    public boolean isAbstraction();
    
    public List<Declaration> getOverloads();
    
    public boolean isDeclaredVoid();

}
