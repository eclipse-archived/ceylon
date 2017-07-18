package com.redhat.ceylon.compiler.java.test.interop;

import java.lang.annotation.Annotation;
import java.util.function.*;
// NOTE: DO NOT REMOVE, apparently we generate a Class type in this damn package
import java.lang.Class;

@FunctionalInterface
interface VariadicFunction {
    public abstract void execute(int... param);
}

@FunctionalInterface
interface OverloadedFunction {
    public default void execute(){
        execute(0);
    }
    public abstract void execute(int param);
}

@FunctionalInterface
interface OverloadedFunction2A {
    public abstract void execute(int param);
}

@FunctionalInterface
interface OverloadedFunction2B extends OverloadedFunction2A {
    public default void execute(){
        execute(0);
    }
}

@FunctionalInterface
interface ConsumeTwoIntegers {
    public abstract void execute(int a, int b);
}

@FunctionalInterface
interface StringFunction {
    public abstract String execute(String a);
}

public class LambdasJava {

    public static class StaticMemberClass{}
    public class MemberClass{}
    
    public LambdasJava(){}
    public LambdasJava(IntConsumer consumer){}

    public static <T> void consumerStatic(Consumer<T> consumer, T t){
        consumer.accept(t);
    }

    public <T> void consumer(Consumer<T> consumer, T t){
        consumer.accept(t);
    }

    public void stringFunction(StringFunction f){
    }

    public <T,R> R function(Function<T,R> function, T t){
        return function.apply(t);
    }

    public void intConsumer(IntConsumer consumer){
        consumer.accept(1);
    }

    public void variadicFunction(VariadicFunction f){
    }

    public int intSupplier(IntSupplier supplier){
        return supplier.getAsInt();
    }

    public void intSuppliers(IntSupplier... suppliers){
    }

    public void overloadedFunction(OverloadedFunction f){
        f.execute();
    }

    public void overloadedFunction2(OverloadedFunction2B f){
        f.execute();
    }

    public void consumeTwoIntegers(ConsumeTwoIntegers f){
    }

    public void stringFunction2(Function<? super String, String> f){
    }

    public void klassMethod(Class<?> k){}

    public <T> Class<T> klassMethodParameterised(Class<T> k){ return k; }
    public <T> Class<? extends T> klassMethodParameterisedCovariant(Class<? extends T> k){ return k; }
    public <T extends LambdasJava> Class<T> klassMethodParameterisedWithBounds(Class<T> k){ return k; }

    public Class<?> getKlass(){ return null; }
    public void setKlass(Class<?> k){}

    public void charSequences(CharSequence... cs){
    }

    public CharSequence getCharSequence(){ return null; }
    public void setCharSequence(CharSequence s){}

    public String getStr(){ return null; }
    public void setStr(String s){}
    
    public static void takeInt(int i){}
    
    final public void synchronizedRun(final Runnable action) {}

    public void takeIterableString(Iterable<String> it){}

    public void takeAnnotation(Annotation at){}
    
    public <T> void arrays(long[] l, double[] d, byte[] b, boolean[] bb, LambdasJava[] lj, T[] t){
        assert(l.length == 2);
        assert(l[0] == 1);
        assert(l[1] == 2);

        assert(d.length == 2);
        assert(d[0] == 1.0);
        assert(d[1] == 2.0);

        assert(b.length == 2);
        assert(b[0] == 1);
        assert(b[1] == 2);

        assert(bb.length == 2);
        assert(bb[0] == true);
        assert(bb[1] == false);

        assert(lj.length == 2);
        assert(lj[0] == this);
        assert(lj[1] == null);

        assert(t.length == 1);
        assert(t[0] == this);
    }
}

interface InterfaceWithCoercedMembers {
    public void m(CharSequence cs, IntSupplier l);
}
