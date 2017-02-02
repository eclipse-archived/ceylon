package com.redhat.ceylon.compiler.java.test.model;

class Bug6803_A {
    public String getAttr() {
        return null;
    }
    public void setAttr(String s) {}
}

class Bug6803_B extends Bug6803_A {
    @Override
    public String getAttr() {
        return null;
    }
}

class Bug6803_C extends Bug6803_A {
    @Override
    public void setAttr(String s) {}
}