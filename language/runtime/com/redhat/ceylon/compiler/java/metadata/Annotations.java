package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Temporary annotation to store ceylon annotations
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotations {
    Annotation[] value() default {};
    long modifiers() default 0;
}
