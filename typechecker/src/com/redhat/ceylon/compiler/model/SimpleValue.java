package com.redhat.ceylon.compiler.model;


/**
 * Represents s simple attribute or local.
 * 
 * @author Gavin King
 *
 */
public class SimpleValue extends Typed {
	Boolean variable;
	Boolean formal;
	
	public Boolean isFormal() {
		return formal;
	}
	
	public void setFormal(Boolean formal) {
		this.formal = formal;
	}
	
	public Boolean isVariable() {
		return variable;
	}
	public void setVariable(Boolean variable) {
		this.variable = variable;
	}
}
