package com.redhat.ceylon.compiler.java.metadata;

public @interface Module {
    public String version();
    public String name();
    public Import[] dependencies() default {};
}
