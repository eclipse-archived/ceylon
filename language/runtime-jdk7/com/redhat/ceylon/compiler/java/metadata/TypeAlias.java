package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a given class is in fact an alias to another type, but not just a class or interface,
 * this can be composite types as well. This is not the same as class aliases or interface aliases.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TypeAlias {
    /**
     * The aliased type signature
     */
    String value();
}
