package com.redhat.ceylon.compiler.metadata.java;

// stef: disabled because the compiler doesn't let us use it otherwise
//@Target(ElementType.PARAMETER)
public @interface Name {
    String value() default "";
}
