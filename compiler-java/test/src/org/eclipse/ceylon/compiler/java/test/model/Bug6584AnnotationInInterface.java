package org.eclipse.ceylon.compiler.java.test.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Bug6584AnnotationInInterface {

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
    @interface MyOtherAnnotation {
        String[] value();
        int position() default 1;
    }
}