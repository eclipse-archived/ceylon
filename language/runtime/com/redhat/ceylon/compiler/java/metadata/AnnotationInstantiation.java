package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * Annotates an annotation constructor method
 * (i.e. adjacent to {@link Method @Method}) 
 * or the default parameter value method of an annotation constructor method 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AnnotationInstantiation {
    /** 
     * The Java class of the annotation class or annotation constructor
     * that the annotation constructor invokes
     */
    java.lang.Class<?> primary();
    
    /** 
     * <p>A permutation of the annotation constructor parameters which produces the 
     * argument list for the instantiation of the annotation class.</p>
     * <table><tbody>
     * <tr><td>-32768</td>  <td>int, char, boolean, float or String literal</td></tr>
     * <tr><td>-32767 - -1</td>  <td>When used as an element of 
     *  {@code @AnnotationInstantiationTree}, this points to another element of the 
     *  tree, which is the invocation whose result is this argument.</td></tr>
     * <tr><td>0-255</td>   <td>the corresponding parameter</td></tr>
     * <tr><td>256-511</td> <td>the corresponding parameter spread</td></tr>
     * </tbody></table>
     */
    short[] arguments() default {};
}
