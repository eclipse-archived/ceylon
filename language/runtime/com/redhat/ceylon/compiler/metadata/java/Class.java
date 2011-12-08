package com.redhat.ceylon.compiler.metadata.java;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks top-level class definitions.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Class {
    String extendsType() default "";
}
