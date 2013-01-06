package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A functional parameter, i.e. a parameter that
 * declares its own parameter list.
 * 
 * @author Gavin King
 *
 */
public class FunctionalParameter extends Parameter implements Scope, Functional {
    
    private List<ParameterList> parameterLists = new ArrayList<ParameterList>();
    private boolean declaredAnything;
    
    @Override
    public List<ParameterList> getParameterLists() {
        return parameterLists;
    }
    
    @Override
    public void addParameterList(ParameterList pl) {
        parameterLists.add(pl);
    }
    
    @Override
    public List<TypeParameter> getTypeParameters() {
        return Collections.emptyList();
    }
    
    @Override
    public boolean isOverloaded() {
    	return false;
    }
    
    @Override
    public boolean isAbstraction() {
        return false;
    }
    
    @Override
    public List<Declaration> getOverloads() {
        return null;
    }
    
    @Override
    public Parameter getParameter(String name) {
        return null;
    }
    
    @Override
    public boolean isDeclaredVoid() {
        return declaredAnything;
    }
    
    public void setDeclaredAnything(boolean declaredAnything) {
        this.declaredAnything = declaredAnything;
    }

}
