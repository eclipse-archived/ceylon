package com.redhat.ceylon.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a setter to be called with the value of a command line 
 * option argument.
 * @see Option
 * @see Argument
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionArgument {
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
    
    /** 
     * The name of this argument (used in doc)
     */
    String argumentName() default "value";
}
