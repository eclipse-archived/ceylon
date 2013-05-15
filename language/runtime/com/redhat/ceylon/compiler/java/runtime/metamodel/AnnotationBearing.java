package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Annotated;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

/**
 * Interface for {@code Free*} and {@code Applied*} implementors of 
 * {@code ceylon.language.metamodel.Annotated} which gets the 
 * Java annotations from the underyling Java reflection object.
 * @author tom
 */
public interface AnnotationBearing extends Annotated {

    /**
     * @return the Java annotations for this Annotated
     */
    @Ignore
    java.lang.annotation.Annotation[] $getJavaAnnotations();
    
}
