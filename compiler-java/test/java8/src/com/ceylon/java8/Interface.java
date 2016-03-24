package com.ceylon.java8;

import com.ceylon.java8.Interface;

public interface Interface {
	public static void staticMethod(){}
	public default void defaultMethod() {
	}
	public interface InnerInterface{
	    public static void staticMethod(){}
	}
    public static class InnerClass{
        public static void staticMethod(){}
    }
}

class InterfaceImpl implements Interface {
    public void defaultMethod(){
        Interface.super.defaultMethod();
        Interface.super.toString();
        super.toString();
    }
}