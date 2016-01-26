package com.redhat.ceylon.compiler.java.test.interop.bug4389.a;

import java.util.Iterator;

public class A implements Iterable<String> {
    public A(){}
    @Override
    public Iterator<String> iterator() {
        throw new RuntimeException();
    }
    public Iterable<String> getIterable() {
        return this;
    }
}