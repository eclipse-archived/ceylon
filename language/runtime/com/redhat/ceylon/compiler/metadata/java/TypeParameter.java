package com.redhat.ceylon.compiler.metadata.java;

public @interface TypeParameter {

    String value();

    String satisfies() default "";

    Variance variance() default Variance.NONE;
}
