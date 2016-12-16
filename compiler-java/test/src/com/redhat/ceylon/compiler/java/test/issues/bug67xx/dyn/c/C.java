package com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.c;

import com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.a.A;
import com.redhat.ceylon.compiler.java.test.issues.bug67xx.dyn.b.B;

public class C {
    public static void main(String[] args){
        new B(new A()).test();
    }
}
