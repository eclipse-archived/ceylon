package com.redhat.ceylon.compiler.java.test.interop;

import java.util.function.*;

public class LambdasJava {

    public LambdasJava(){}
    public LambdasJava(IntConsumer consumer){}
    
    public <T> void consumer(Consumer<T> consumer, T t){
        consumer.accept(t);
    }

    public <T,R> R function(Function<T,R> function, T t){
        return function.apply(t);
    }

    public void intConsumer(IntConsumer consumer){
        consumer.accept(1);
    }

    public int intSupplier(IntSupplier supplier){
        return supplier.getAsInt();
    }
    
    public CharSequence getCharSequence(){ return null; }
    public void setCharSequence(CharSequence s){}

    public String getStr(){ return null; }
    public void setStr(String s){}
    
    public static void takeInt(int i){}
}
