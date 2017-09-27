package org.eclipse.ceylon.compiler.java.test.issues.bug67xx.dyn.c;

import org.eclipse.ceylon.compiler.java.test.issues.bug67xx.dyn.a.A;
import org.eclipse.ceylon.compiler.java.test.issues.bug67xx.dyn.b.B;

public class C {
    public static void main(String[] args){
        new B(new A()).test();
    }
}
