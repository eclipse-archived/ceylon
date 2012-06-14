package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks types as being generated Ceylon-based
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Ceylon {
    /**
     * Ceylon binary interface major number.
     */
    int major() default 0;
    /**
     * Ceylon binary interface minor number.
     */
    int minor() default 0;
}
