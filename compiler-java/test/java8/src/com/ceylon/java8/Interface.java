package com.ceylon.java8;

import com.ceylon.java8.Interface;

public interface Interface {
	public static void staticMethod(){}
	public default void defaultMethod() {
	}
}

class InterfaceImpl implements Interface {
    public void defaultMethod(){
        Interface.super.defaultMethod();
        Interface.super.toString();
        super.toString();
    }
}