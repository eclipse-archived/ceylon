package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.AnnotationInstantiation;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;

@Ceylon(major = 8)
@Method
@AnnotationInstantiation(
        arguments = {},
        primary = Unserializable.class)
public class unserializable_ {
    private unserializable_() {
    }
    @ceylon.language.AnnotationAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    public static Unserializable $unserializable() {
        return new Unserializable();
    }
}
