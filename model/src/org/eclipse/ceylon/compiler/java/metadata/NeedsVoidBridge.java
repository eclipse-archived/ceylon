package org.eclipse.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates a void bridge method to this non-void method. Use with caution. 
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface NeedsVoidBridge {

}
