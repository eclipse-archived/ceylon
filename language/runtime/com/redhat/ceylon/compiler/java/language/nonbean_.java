package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.AnnotationInstantiation;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;

@Ceylon(major = 8)
@Method
@AnnotationInstantiation(
        arguments = {},
        primary = Nonbean.class)
public class nonbean_ {
    private nonbean_() {
    }
    @ceylon.language.AnnotationAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    public static Nonbean $nonbean() {
        return new Nonbean();
    }
}
