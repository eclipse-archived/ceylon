package com.redhat.ceylon.common.tool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>Annotates a {@link Tool Tool} and/or its {@link Option @Option}-annotated 
 * setters to say that the annotated element should be considered 
 * "low-level".</p>
 * 
 * <p>Low level tools and options should not normally be shown to users in help 
 * output etc.</p> 
 * 
 * @see Summary
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Hidden {
}
