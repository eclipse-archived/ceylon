package com.redhat.ceylon.compiler.metadata.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
public @interface Name {
    String value() default "";
}
