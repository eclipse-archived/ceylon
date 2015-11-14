package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to a default parameter value method, 
 * the integer default value.
 * 
 * Within a {@link IntegerExprs}, the integer value of the "literal" 
 * expression.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface IntegerValue {
    String name() default "";
    long[] value();
}
