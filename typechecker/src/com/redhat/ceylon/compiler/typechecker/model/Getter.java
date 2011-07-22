package com.redhat.ceylon.compiler.typechecker.model;


/**
 * An attribute getter.
 *
 * @author Gavin King
 */
public class Getter extends MethodOrValue implements Scope {

    boolean variable;
    Setter setter;

    public Setter getSetter() {
        return setter;
    }

    public void setSetter(Setter setter) {
        this.setter = setter;
    }

    @Override
    public boolean isVariable() {
        return variable;
    }

    public void setVariable(boolean variable) {
        this.variable = variable;
    }
    
}
