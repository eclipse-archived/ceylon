package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Lists all the local declaration Java class names, relative to the current
 * Java class name.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDeclarations {
    String[] value() default {};
}
