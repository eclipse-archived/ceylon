package com.redhat.ceylon.compiler.typechecker.model;


/**
 * An attribute getter.
 *
 * @author Gavin King
 */
public class Getter extends MethodOrValue implements Scope {

    Setter setter;

    public Setter getSetter() {
        return setter;
    }

    public void setSetter(Setter setter) {
        this.setter = setter;
    }

    @Override
    public boolean isVariable() {
        return setter!=null;
    }
    
}
