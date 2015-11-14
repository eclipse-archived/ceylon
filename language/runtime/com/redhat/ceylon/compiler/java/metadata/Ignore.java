package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation applied to a Java element that should be ignored by the Ceylon 
 * model loader.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
}
