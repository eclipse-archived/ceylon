package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks top-level class definitions.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Class {
    /** The Ceylon superclass of the {@code @Class}-annotated Ceylon type */
    String extendsType() default "ceylon.language.IdentifiableObject";
}
