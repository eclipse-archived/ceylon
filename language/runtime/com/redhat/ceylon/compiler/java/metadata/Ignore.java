package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks that the associated item should be ignored by the model loader.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
}
