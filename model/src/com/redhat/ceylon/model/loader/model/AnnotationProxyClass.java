package com.redhat.ceylon.model.loader.model;

import java.util.EnumSet;
import java.util.List;

import com.redhat.ceylon.model.loader.mirror.AnnotationMirror;
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
    public EnumSet<AnnotationTarget> getAnnotationTarget() {
        AnnotationMirror targetAnno = iface.classMirror.getAnnotation("java.lang.annotation.Target");
        if (targetAnno != null) {
            List<String> targets = (List)targetAnno.getValue();
            EnumSet<AnnotationTarget> result = EnumSet.<AnnotationTarget>noneOf(AnnotationTarget.class);
            for (String name : targets) {
                result.add(AnnotationTarget.valueOf(name));
            }
            return result;
        }
        return null;
    }

}
