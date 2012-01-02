package com.redhat.ceylon.compiler.java.metadata;

public @interface TypeParameter {

    String value();

    String[] satisfies() default {};

    Variance variance() default Variance.NONE;
}
