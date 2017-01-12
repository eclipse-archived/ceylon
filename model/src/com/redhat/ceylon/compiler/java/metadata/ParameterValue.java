package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to a default parameter value method, 
 * the parameter default value.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface ParameterValue {
    // Note that unlike the other @*Value annotations, this parameters as 
    // "static" arguments are handled via @AnnotationInstantiation, so there's no
    // corresponding @*Exprs annotation, and no need for a String name() member here.
    String[] value();
}
