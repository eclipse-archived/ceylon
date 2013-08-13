package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to store an defaulted annotation constructor parameter default value
 * when it is a enumeration value (i.e. anonymous subclass of an enumerated type).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultedObject {
    java.lang.Class<?> value();
}
