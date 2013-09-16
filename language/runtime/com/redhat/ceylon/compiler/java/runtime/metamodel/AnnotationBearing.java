package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Annotated;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

/**
 * Interface for {@code Free*} and {@code Applied*} implementors of 
 * {@code ceylon.language.meta.model.Annotated} which gets the 
 * Java annotations from the underyling Java reflection object.
 * @author tom
 */
public interface AnnotationBearing extends Annotated {

    static final java.lang.annotation.Annotation[] NONE = new java.lang.annotation.Annotation[0];
    
    /**
     * @return the Java annotations for this Annotated
     */
    @Ignore
    java.lang.annotation.Annotation[] $getJavaAnnotations();
    
}
