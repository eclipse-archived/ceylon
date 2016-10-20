package com.redhat.ceylon.compiler.java.test.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Bug6584AnnotationInClass {

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.TYPE})
    @interface MyAnnotation {
        String[] value();
    }
}
