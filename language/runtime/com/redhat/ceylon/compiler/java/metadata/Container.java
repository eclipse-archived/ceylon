package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation applied to member types who have been extracted out of their containers.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Container {
    
    /** 
     * Name of the container type
     */
    String name();
    
    /**
     * FQN of the java class we generated for the container type 
     */
    String javaClass();
    
    /**
     * Package part of the javaClass
     */
    String packageName();
}
