package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.AnnotationInstantiation;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Method;

import org.eclipse.ceylon.compiler.java.language.Synchronized;


@Ceylon(major = 8)
@Method
@AnnotationInstantiation(
        arguments = {},
        primary = Synchronized.class)
public class synchronized_ {
    private synchronized_() {
    }
    @ceylon.language.AnnotationAnnotation$annotation$
    @org.eclipse.ceylon.common.NonNull
    public static Synchronized $synchronized() {
        return new Synchronized();
    }
}
