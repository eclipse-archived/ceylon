package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java interface that is a Ceylon interface.
 * @since Ceylon 1.2.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Interface {
    /** 
     * The interface uses default methods, rather than companion class to 
     * handle concrete members.
     */
    boolean useDefaultMethods() default false;
}
