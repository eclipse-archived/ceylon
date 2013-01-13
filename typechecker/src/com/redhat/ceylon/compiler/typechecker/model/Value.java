package com.redhat.ceylon.compiler.typechecker.model;


/**
 * Represents s simple attribute or local.
 *
 * @author Gavin King
 */
public class Value extends MethodOrValue {

    private boolean variable;
    private boolean trans;
    private boolean captured;
    private boolean late;

    /*public boolean isFormal() {
         return formal;
     }

     public void setFormal(boolean formal) {
         this.formal = formal;
     }*/

    @Override
    public boolean isVariable() {
        return variable;
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
    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean local) {
        this.captured = local;
    }
    
    @Override
    public boolean isLate() {
		return late;
	}
    
    public void setLate(boolean late) {
		this.late = late;
	}

}
