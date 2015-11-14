package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Temporary annotation to store ceylon annotations. Should be contained in a 
 * {@link Annotations @Annotations()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Annotation {
    String value();
    String[] arguments() default {};
    NamedArgument[] namedArguments() default {};
}
