package com.redhat.ceylon.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a setter to be called with the value of a command line argument
 * @see Option
 * @see OptionArgument
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {

    /** 
     * The name of this argument (used in doc)
     */
    String argumentName() default "value";
    
    /**
     * The number of times this argument can appear in a command line
     */
    String multiplicity();
    
    /**
     * The position of this argument relative to other {@code @Argument}s 
     * in the argument list. It is an error for two occurances of @Argument
     * in a class to have the same order.
     */
    int order() default 0;
}
