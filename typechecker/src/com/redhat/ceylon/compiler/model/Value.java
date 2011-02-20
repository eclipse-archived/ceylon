package com.redhat.ceylon.compiler.model;


/**
 * Represents s simple attribute or local.
 * 
 * @author Gavin King
 *
 */
public class Value extends MethodOrValue {
    
	boolean variable;
	boolean formal;
	
	public boolean isFormal() {
		return formal;
	}
	
	public void setFormal(boolean formal) {
		this.formal = formal;
	}
	
	public boolean isVariable() {
		return variable;
	}
	public void setVariable(boolean variable) {
		this.variable = variable;
	}
	
}
