package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A type parameter of a Ceylon type or method (should be contained in an
 * {@link TypeParameters @TypeParameters()}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface TypeParameter {
    
    /** The name of the type parameter */
    String value();
    
    /** 
     * String representation of the types that this type parameter is 
     * constrained to satisfy (in the {@code given ... satisfies ...} clause). 
     * May contain fully-qualified type names and type parameter names 
     */
    String[] satisfies() default {};

    /** 
     * String representation of the case types that this type parameter is 
     * constrained to be (in the {@code given ... of ...} clause). 
     * May contain fully-qualified type names and type parameter names 
     */
    String[] caseTypes() default {};

    /** The variance of this type parameter */
    Variance variance() default Variance.NONE;
}
