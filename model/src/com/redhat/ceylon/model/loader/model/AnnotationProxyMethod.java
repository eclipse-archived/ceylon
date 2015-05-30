package com.redhat.ceylon.model.loader.model;

import com.redhat.ceylon.model.typechecker.model.Function;

/**
 * Used for annotation interop.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationProxyMethod extends Function {
    
    private AnnotationProxyClass proxyClass;
    
    private OutputElement annotationTarget;
    
    public AnnotationProxyClass getProxyClass() {
        return proxyClass;
    }
    
    public void setProxyClass(AnnotationProxyClass proxyClass) {
        this.proxyClass = proxyClass;
    }

    public void setAnnotationTarget(OutputElement annotationTarget) {
        this.annotationTarget = annotationTarget;
    }
    
    /**
     * If this is a disambiguating proxy annotation method, then this is the 
     * Java program element that the constructor targets. Otherwise null
     */
    public OutputElement getAnnotationTarget() {
        return this.annotationTarget;
    }

}
