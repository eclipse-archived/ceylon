package com.redhat.ceylon.compiler.metadata.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface TypeInfo {
    String value() default "";
}
