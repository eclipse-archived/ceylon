package com.redhat.ceylon.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.redhat.ceylon.tools.Plugin;

/**
 * Annotates a {@link Plugin#run()} method, documenting its exit codes. 
 */
@Target({ElementType.METHOD})
public @interface ExitCodes {
    ExitCode[] value();
}
