package com.redhat.ceylon.compiler.metadata.java;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Ceylon {
    // Marks types as being generated Ceylon-based
}
