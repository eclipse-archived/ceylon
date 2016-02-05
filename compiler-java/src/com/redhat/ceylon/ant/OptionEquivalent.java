package com.redhat.ceylon.ant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * Annotation on ant attribute setter/adder which is 
 * equivalent to an option of a {@code ceylon} CLI tool.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) 
public @interface OptionEquivalent {
    /** 
     * The full long-option name (e.g. {@code --foo-bar} of 
     * the corresponding option. If empty the long option name will 
     * be inferred from the name of the annotated method via
     * camel-case to dashed conversion.
     */
    String value() default "";
    
    /**
     * Whether to link to the option in the tool's documentation 
     */
    boolean link() default true;
    
    /**
     * Whether to transclude the option documentation from the tool's documentation
     */
    boolean transclude() default true;
}