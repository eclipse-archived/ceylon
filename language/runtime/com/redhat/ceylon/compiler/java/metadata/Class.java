package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks top-level class definitions.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Class {
    String extendsType() default "ceylon.language.IdentifiableObject";
}
