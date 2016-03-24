package com.redhat.ceylon.ant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** An attribution which should not be documented */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD}) 
public @interface AntDocIgnore {
}