package com.redhat.ceylon.tools.annotation;

import java.lang.annotation.Target;

/**
 * Documents a tools exit code (used within an {@link ExitCodes})
 */
@Target({/*nothing*/})
public @interface ExitCode {

    int value();
    String doc() default "";
    
}
