package com.redhat.ceylon.compiler.java.metadata;

/**
 * Temporary annotation to store ceylon annotations.
 * @see Annotation
 */
public @interface NamedArgument {
    String name();
    String value();
}
