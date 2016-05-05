package com.redhat.ceylon.compiler.java.test.interop;

class ArgType1 {}

class ArgType2 {}

class Base1 {
	public void add(ArgType1... argType1s) {}
}

class Base2 extends Base1 {
	public void add(ArgType2... argType2s) {}
}

