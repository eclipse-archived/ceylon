package com.redhat.ceylon.common.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotates a {@link Tool Tool} and its {@link Option @Option}-annotated 
 * setters with Markdown-formatted documentation.
 * 
 * @see Summary
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Description {
    /** Documentation on the annotated element, in Markdown format */
    String value();
}
