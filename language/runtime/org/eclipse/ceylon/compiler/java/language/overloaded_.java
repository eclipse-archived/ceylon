package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.AnnotationInstantiation;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Method;

import org.eclipse.ceylon.compiler.java.language.Overloaded;

@Ceylon(major = 8)
@Method
@AnnotationInstantiation(
        arguments = {},
        primary = Overloaded.class)
public class overloaded_ {
    private overloaded_() {
    }
    @ceylon.language.AnnotationAnnotation$annotation$
    @org.eclipse.ceylon.common.NonNull
    public static Overloaded $overloaded() {
        return new Overloaded();
    }
}
