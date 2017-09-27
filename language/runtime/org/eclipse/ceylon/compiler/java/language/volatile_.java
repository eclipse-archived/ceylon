package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.AnnotationInstantiation;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Method;

import org.eclipse.ceylon.compiler.java.language.Volatile;

@Ceylon(major = 8)
@Method
@AnnotationInstantiation(
        arguments = {},
        primary = Volatile.class)
public class volatile_ {
    private volatile_() {
    }
    @ceylon.language.AnnotationAnnotation$annotation$
    @org.eclipse.ceylon.common.NonNull
    public static Volatile $volatile() {
        return new Volatile();
    }
}
