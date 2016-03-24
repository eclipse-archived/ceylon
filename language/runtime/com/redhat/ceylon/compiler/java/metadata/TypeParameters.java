package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java type or method for holding multiple 
 * {@link TypeParameter @TypeParameter} annotations.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TypeParameters {

    /** The Ceylon type parameters of the annotated type or method. */
    TypeParameter[] value();

}
