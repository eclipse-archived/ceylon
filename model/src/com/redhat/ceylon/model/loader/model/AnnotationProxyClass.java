package com.redhat.ceylon.model.loader.model;

import java.util.EnumSet;

import com.redhat.ceylon.model.typechecker.model.Class;

/**
 * Used for annotation proxies for interop.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationProxyClass extends Class {

    public final LazyInterface iface;

    public AnnotationProxyClass(LazyInterface iface) {
        this.iface = iface;
    }
    
    /**
     * The elements in the {@code @Target} annotation, or null if 
     * the annotation type lacks the {@code @Target} annotation.
     */
    @Override
    public EnumSet<AnnotationTarget> getAnnotationTarget() {
        return AnnotationTarget.getAnnotationTarget(iface);
    }
}
