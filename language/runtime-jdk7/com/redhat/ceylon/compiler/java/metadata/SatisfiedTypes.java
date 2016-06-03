package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java type recording the Ceylon type it satisfies.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SatisfiedTypes {
    /**
     * String representation of the types that this type is 
     * constrained to satisfy (in the {@code given ... satisfies ...} clause).
     */
    String[] value() default {};
}
