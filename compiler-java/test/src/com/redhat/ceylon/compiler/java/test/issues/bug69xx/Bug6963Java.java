package com.redhat.ceylon.compiler.java.test.issues.bug69xx;

public class Bug6963Java<T extends String> {
    public interface Inner {
        void fun(Bug6963Java<?> java);
    }
}