package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to the Java class which is used to disambiguate 
 * multiple named constructors.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ConstructorName {
    /** The Ceylon name of the constructor name. */
    String value();
    /** true iff this is the constructor name class for a delegation constructor */
    boolean delegation() default false;
}
