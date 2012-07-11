package com.redhat.ceylon.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a setter which gets a list of all the command line 
 * options which are not bound using @Option, @Argument or @ArgumentList
 * @author tom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Rest {

}
