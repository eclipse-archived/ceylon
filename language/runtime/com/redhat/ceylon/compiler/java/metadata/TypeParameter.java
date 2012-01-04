package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TypeParameter {

    String value();

    String[] satisfies() default {};

    Variance variance() default Variance.NONE;
}
