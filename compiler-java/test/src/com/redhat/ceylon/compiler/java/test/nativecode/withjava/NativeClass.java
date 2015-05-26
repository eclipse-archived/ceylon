package com.redhat.ceylon.compiler.java.test.nativecode.withjava;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

//Compiled from run.ceylon (version 1.7 : 51.0, super bit)
@com.redhat.ceylon.compiler.java.metadata.Ceylon(major=8)
@ceylon.language.NativeAnnotation$annotation$(backend="java")
@ceylon.language.SharedAnnotation$annotation$
public class NativeClass implements com.redhat.ceylon.compiler.java.runtime.model.ReifiedType {

// Field descriptor #9 Lcom/redhat/ceylon/compiler/java/runtime/model/TypeDescriptor;
@com.redhat.ceylon.compiler.java.metadata.Ignore
public static final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $TypeDescriptor$;

// Method descriptor #13 (Ljava/lang/String;)V
// Stack: 1, Locals: 2
public NativeClass(
	@com.redhat.ceylon.compiler.java.metadata.Name("s")
	@com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::String")
	java.lang.String s) {
	System.out.println(s);
}

// Method descriptor #27 ()Lcom/redhat/ceylon/compiler/java/runtime/model/TypeDescriptor;
// Stack: 1, Locals: 1
@com.redhat.ceylon.compiler.java.metadata.Ignore
public com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
	return $TypeDescriptor$;
}

// Method descriptor #29 ()V
// Stack: 2, Locals: 0
static {
	$TypeDescriptor$ = TypeDescriptor.klass(NativeClass.class);
}
}
