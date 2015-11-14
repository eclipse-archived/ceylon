package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a parameter of {@code Callable} type as being a functional parameter
 * (i.e. having {@code Method} as its model)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface FunctionalParameter {
    /** 
     * <p>The names of the parameters of this functional parameter. 
     * Because a functional parameter can itself have functional parameters,
     * and because of the possibility of methods with multiple 
     * parameter lists this is not a simple name. In general values should
     * conform to the {@code input} production of the following grammar:</p>
     *  
     * <pre>
     * input     ::= nameList ( nameList )*
     * nameList  ::= '(' ( name ( ',' name )* )? ')'
     * name      ::= identifier ( nameList )*
     * </pre>
     * <ul>
     *   <li>A {@code !} means that the {@code Method} model is declared {@code void}</li>
     *   <li>A {@code +} means that the {@code Parameter} model is possibly-empty variadic</li>
     *   <li>A {@code *} means that the {@code Parameter} model is nonempty variadic</li>
     * </ul> 
     */
    String value() default "";
}
