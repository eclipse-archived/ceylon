package com.redhat.ceylon.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.redhat.ceylon.tools.Plugin;

/**
 * Annotates a {@link Plugin Tool} and its setters (which should be 
 * {@link Option @Option}-annotated) containing their help doc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Description {
    String value();
}
