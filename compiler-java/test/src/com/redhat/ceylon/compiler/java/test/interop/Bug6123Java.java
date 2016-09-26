package com.redhat.ceylon.compiler.java.test.interop;

class Tree6123 {

    static class Term {}
    static class Plus extends Term {}
    static class Minus extends Term {}

}

class Visitor6123 {
    void visit(Tree6123.Plus plus) {}
}