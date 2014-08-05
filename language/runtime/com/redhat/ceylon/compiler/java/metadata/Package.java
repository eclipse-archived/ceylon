package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java class 
 * also annotated with {@link Attribute @Attribute} which holds the runtime 
 * package descriptor instance.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Package {
    /** Whether the package is shared */
    public boolean shared();
    /** The name of the package */
    public String name();
    /** The package documentation. */
    public String doc() default "";
    /** The module author(s). */
    public String[] by() default {};
    /** The module license */
    public String license() default "";
}
