package com.redhat.ceylon.compiler.impl;

public abstract class NamedArgumentCall<X> {
    protected final X instance;
    public NamedArgumentCall(final X instance, Object... args) {
        this.instance = instance;
    }
}
