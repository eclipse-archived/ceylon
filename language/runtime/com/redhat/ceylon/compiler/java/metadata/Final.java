package com.redhat.ceylon.compiler.java.metadata;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * In EE mode we don't generate {@code final} methods, but the model loader
 * uses Java {@code final} to infer Ceylon {@code default}. So in EE mode we annotate 
 * the non-{@code final}, non-{@code default} methods with @Final.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Final {

}
