package com.redhat.ceylon.compiler.typechecker.model;


/**
 * Represents a simple attribute or local.
 *
 * @author Gavin King
 */
public class Value extends MethodOrValue {

    private boolean variable;
    private boolean trans;
    private boolean late;

    private Setter setter;

    public Setter getSetter() {
        return setter;
    }
    
    @Override
    public void setSetter(Setter setter) {
        this.setter = setter;
    }
    
    @Override
    public boolean isVariable() {
        return variable || setter!=null;
    }

    public void setVariable(boolean variable) {
        this.variable = variable;
    }

    @Override
    public boolean isTransient() {
        return trans;
    }
    
    public void setTransient(boolean trans) {
    	this.trans = trans;
    }
    
    @Override
    public boolean isLate() {
		return late;
	}
    
    public void setLate(boolean late) {
		this.late = late;
	}

}
