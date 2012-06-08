package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    public String version();
    public String name();
    public String doc() default "";
    public String[] by() default {};
    public String license() default "";
    public Import[] dependencies() default {};
}