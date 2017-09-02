package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Ceylon(major = 8)
@Class
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface Nonbean$annotation$ {
}