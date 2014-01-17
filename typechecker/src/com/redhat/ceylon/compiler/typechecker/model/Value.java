package com.redhat.ceylon.compiler.typechecker.model;


/**
 * Represents a simple attribute or local.
 *
 * @author Gavin King
 */
public class Value extends MethodOrValue implements Scope {

    private boolean variable;
    private boolean trans;
    private boolean late;
    private boolean enumValue;
    private boolean specifiedInForElse;

    private Setter setter;
    // used for object declarations that use their own value binding in their body
    private boolean selfCaptured;

    public Setter getSetter() {
        return setter;
    }
    
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

    public boolean isEnumValue() {
        return enumValue;
    }

    public void setEnumValue(boolean enumValue) {
        this.enumValue = enumValue;
    }

    public boolean isSpecifiedInForElse() {
        return specifiedInForElse;
    }

    public void setSpecifiedInForElse(boolean assignedInFor) {
        this.specifiedInForElse = assignedInFor;
    }

    @Override
    public boolean isSelfCaptured(){
        return selfCaptured;
    }
    
    public void setSelfCaptured(boolean selfCaptured) {
        this.selfCaptured = selfCaptured;
    }

}
