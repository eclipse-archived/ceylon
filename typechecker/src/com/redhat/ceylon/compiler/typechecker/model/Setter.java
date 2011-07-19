package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

/**
 * An attribute setter.
 *
 * @author Gavin King
 */
public class Setter extends MethodOrValue implements Scope {

    Getter getter;

    public Getter getGetter() {
        return getter;
    }

    public void setGetter(Getter getter) {
        this.getter = getter;
    }

    @Override @Deprecated
    public List<String> getQualifiedName() {
        return getter.getQualifiedName();
    }

    @Override
    public String getQualifiedNameString() {
        return getter.getQualifiedNameString();
    }
    
    @Override
    public boolean isShared() {
        if (getter!=null) {
            return getter.isShared();
        }
        else {
            return super.isShared();
        }
    }
    
    @Override
    public boolean isVariable() {
        return true;
    }
    
}
