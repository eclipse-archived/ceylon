package com.redhat.ceylon.compiler.java.test.issues.bug63xx.bug6310;

abstract class Top {
    abstract void run();
}

abstract class Intervening extends Top {
    // Not an overload, it's static!
    public static int run(Object o) {
        return 1;
    }
}