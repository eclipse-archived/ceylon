package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Temporary annotation to store ceylon annotations
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation {
    String value();
    String[] arguments() default {};
    NamedArgument[] namedArguments() default {};
}
