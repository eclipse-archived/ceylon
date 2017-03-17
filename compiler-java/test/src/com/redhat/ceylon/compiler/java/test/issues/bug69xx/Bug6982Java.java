package com.redhat.ceylon.compiler.java.test.issues.bug69xx;

import java.util.function.Function;
import java.util.function.IntFunction;

public class Bug6982Java {
    public static void fun1(Function<String, String> fun) {
        System.out.print(fun.apply("hello world"));
    }
}