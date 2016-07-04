package com.redhat.ceylon.compiler.java.test.interop;

import java.util.function.Consumer;

public class LambdasJava {

    public <T> void foo(Consumer<T> consumer, T t){
        consumer.accept(t);
    }
}
