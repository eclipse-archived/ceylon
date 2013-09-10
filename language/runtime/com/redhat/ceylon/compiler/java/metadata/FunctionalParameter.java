package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Marks a parameter of {@code Callable} type as being a functional parameter
 *  (i.e. having {@code Method} as its model)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface FunctionalParameter {

}
