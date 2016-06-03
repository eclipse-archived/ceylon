package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java class that is a container for a 
 * top level Ceylon attribute.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Attribute {
    /**
     * Used by local attributes to point to the class holding the setter definition.
     */
    java.lang.Class<?> setterClass() default void.class;
}
