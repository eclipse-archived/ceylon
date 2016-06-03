package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java class that is a container for a local setter attribute.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Setter {
    /**
     * Used by local attributes to point to the class holding the getter definition.
     */
    java.lang.Class<?> getterClass() default void.class;
}
