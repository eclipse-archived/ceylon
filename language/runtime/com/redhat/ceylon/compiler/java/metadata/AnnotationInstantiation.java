package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * Annotates an annotation constructor method
 * (i.e. adjacent to {@link Method @Method}) 
 * as being a trivial instantiation of an annotation class. 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AnnotationInstantiation {
    /** 
     * The annotation class (not the annotation type for that class) 
     * that the annotation constructor instantiates 
     */
    // TODO This might not be needed
    java.lang.Class<?> annotationClass();
    
    // TODO Can also support a constructor which returns a value using some 
    // other annotation @AnnotationValue
    /** 
     * <p>A permutation of the annotation constructor parameters which produces the 
     * argument list for the instantiation of the annotation class.</p>
     * <table><tbody>
     * <tr><td>-32768</td>  <td>int, char, boolean, float or String literal</td></tr>
     * <tr><td>0-255</td>   <td>the corresponding parameter</td></tr>
     * <tr><td>256-511</td> <td>the corresponding parameter spread</td></tr>
     * </tbody></table>
     */
    short[] arguments() default {};
}
