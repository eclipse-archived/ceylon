package com.redhat.ceylon.common.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotates a {@link Tool Tool} with a Markdown-formatted summary.
 * 
 * @see Description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Summary {
    /** 
     * The Markdown formatted summary. This should be a single sentence 
     * describing what the tool does. 
     */
    String value();
    
}
