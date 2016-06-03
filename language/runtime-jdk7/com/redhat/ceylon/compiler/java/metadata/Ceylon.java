package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java type that have should be treated by the 
 * Ceylon model loader has a Ceylon type, rather than plain Java type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Ceylon {
    /**
     * Ceylon binary interface major number.
     */
    int major() default 0;
    /**
     * Ceylon binary interface minor number.
     */
    int minor() default 0;
}
