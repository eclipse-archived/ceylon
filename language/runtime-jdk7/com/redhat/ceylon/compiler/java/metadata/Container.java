package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation applied to member types who have been extracted out of their containers.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Container {
    
    /**
     * The container class.
     */
    java.lang.Class<?> klass();
}
