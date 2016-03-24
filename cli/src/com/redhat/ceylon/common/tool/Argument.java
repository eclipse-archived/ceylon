package com.redhat.ceylon.common.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a setter to be called with the value of a command line 
 * <em>argument</em>
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
     * The number of times this argument can appear in a command line. 
     * <p>Allowed syntax:</p>
     * <dl>
     * <dt><code>?</code></dt>
     * <dd>argument must appear 0 or 1 times</dd>
     * <dt><code>*</code></dt>
     * <dd>argument must appear 0 or more times</dd>
     * <dt><code>+</code></dt>
     * <dd>argument must appear 1 or more times</dd>
     * <dt><var>n</var></dt>
     * <dd>where <var>n</var> is a positive decimal integer: argument must appear exactly <var>n</var> times</dd>
     * <dt><code>[<var>n</var>,]</code></dt>
     * <dd>where <var>n</var> is a positive decimal integer: argument must appear <var>n</var> times or more</dd>
     * <dt><code>[<var>n</var>,<var>m</var>]</code></dt>
     * <dd>where <var>n</var> and <var>m</var> are a positive decimal integers: 
     * argument must appear between <var>n</var> and <var>m</var> times, inclusive</dd>
     * </dl>
     * 
     */
    String multiplicity();
    
    /**
     * The position of this argument relative to other {@code @Argument}s 
     * in the argument list. It is an error for two occurances of @Argument
     * in a class to have the same order.
     */
    int order() default 0;
}
