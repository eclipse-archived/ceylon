package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.AnnotationInstantiation;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Method;

import org.eclipse.ceylon.compiler.java.language.Transient;

 @Ceylon(major = 8)
@Method
@AnnotationInstantiation(
        arguments = {},
        primary = Transient.class)
public class transient_ {
    private transient_() {
    }
    @ceylon.language.AnnotationAnnotation$annotation$
    @org.eclipse.ceylon.common.NonNull
    public static Transient $transient() {
        return new Transient();
    }
}
