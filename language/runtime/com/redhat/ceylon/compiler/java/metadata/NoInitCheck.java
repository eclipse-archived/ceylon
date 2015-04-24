package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Prevent javac from performing its usual initialization checks on the 
 * annotation variable or field. <strong>Use with caution<strong>. 
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface NoInitCheck {

}
