package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to a default parameter value method, 
 * the string default value.
 * 
 * Within a {@link StringExprs}, the string value of the "literal" 
 * expression.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface StringValue {
    String name() default "";
    String[] value();
}
