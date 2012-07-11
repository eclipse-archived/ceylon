package com.redhat.ceylon.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a setter to be called with the value true if the option appears in 
 * a command line.
 * @see OptionArgument
 * @see Argument
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Option {

    public static final char NO_SHORT = '\0';
    
    /** 
     * The name of the option. 
     * Defaults to a name based on the setter name
     */
    String longName() default "";
    
    /**
     * The short option. If not given the option has no short name.
     */
    char shortName() default NO_SHORT;
    
}
