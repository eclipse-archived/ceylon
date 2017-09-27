package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.AnnotationInstantiation;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Method;

import org.eclipse.ceylon.compiler.java.language.Nonbean;

@Ceylon(major = 8)
@Method
@AnnotationInstantiation(
        arguments = {},
        primary = Nonbean.class)
public class nonbean_ {
    private nonbean_() {
    }
    @ceylon.language.AnnotationAnnotation$annotation$
    @org.eclipse.ceylon.common.NonNull
    public static Nonbean $nonbean() {
        return new Nonbean();
    }
}
