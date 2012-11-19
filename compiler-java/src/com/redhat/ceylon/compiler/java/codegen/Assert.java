package com.redhat.ceylon.compiler.java.codegen;

class Assert {
    static RuntimeException fail(String message) {
        throw new RuntimeException(message);
    }
    static RuntimeException fail() {
        return fail(null);
    }
    static void that(boolean cond, String message) {
        if (!cond) {
            fail(message);
        }
    }
    static void that(boolean cond) {
        that(cond, null);
    }
    static void not(boolean cond, String message) {
        if (cond) {
            fail(message);
        }
    }
    static void not(boolean cond) {
        not(cond, null);
    }
}
