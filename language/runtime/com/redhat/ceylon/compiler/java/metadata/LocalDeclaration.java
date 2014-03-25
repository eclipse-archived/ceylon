package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation applied to local declarations
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalDeclaration {
    String qualifier() default "";
}
