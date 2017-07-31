package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation applied to a Java element that should be ignored by the Ceylon 
 * model loader.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
    /**
     * Use this to mark non-generated `@Ignore`d constructs, which is useful so make
     * code that looks at `@Ignore`d constructs (in the metamodel) really ignore it.
     */
    boolean handWritten() default false;
}
