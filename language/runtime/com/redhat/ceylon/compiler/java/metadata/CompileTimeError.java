package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to a Java class which had a compile time error but 
 * got generated anyway because of error recovery.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CompileTimeError {
}
