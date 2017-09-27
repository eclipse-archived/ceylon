package org.eclipse.ceylon.compiler.java.language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;

@Ceylon(major = 8)
@Class
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Transient$annotation$ {
}