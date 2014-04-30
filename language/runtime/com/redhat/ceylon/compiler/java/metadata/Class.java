package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java class that is a Ceylon class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Class {
    /** 
     * The Ceylon superclass of the {@code @Class}-annotated Ceylon type.
     * Defaults to your Java super class, or ceylon.language::Basic if it is java.lang.Object
     */
    String extendsType() default "";
    
    /**
     * Set to false if this class does not extend ceylon.language::Basic. Defaults to true.
     */
    boolean basic() default true;

    /**
     * Set to false if this class does not implement ceylon.language::Identifiable. Defaults to true.
     */
    boolean identifiable() default true;
}
