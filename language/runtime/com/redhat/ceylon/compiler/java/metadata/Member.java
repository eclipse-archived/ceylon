package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation applied to an interface in order to list all its members
 * that have been extracted out of it.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Member {
    
    /**
     * The member class
     */
    java.lang.Class<?> klass();
}
