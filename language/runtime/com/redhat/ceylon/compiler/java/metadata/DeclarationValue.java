package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applied to a default parameter value method, 
 * the declaration (metamodel) default value.
 * 
 * Within a {@link DeclarationExprs}, the declaration value of the "literal" 
 * expression.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DeclarationValue {
    String name() default "";
    String[] value();
}
