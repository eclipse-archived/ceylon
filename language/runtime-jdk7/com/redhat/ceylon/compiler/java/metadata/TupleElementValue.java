package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The value of an element in a {@link TupleValue}.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface TupleElementValue {
    /** The type of this element */
    TupleElementType type();
    /** The String value of the element, if type == STRING */
    String s() default "";
    /** The Boolean value of the element, if type == BOOLEAN */
    boolean b() default false;
    /** The Float value of the element, if type == FLOAT */
    double f() default 0.0;
    /** The Integer value of the element, if type == Integer */
    long i() default 0;
    /** The Character value of the element, if type == CHARACTER */
    int c() default 0;
    /** The Declaration value of the element, if type == DECLARATION */
    String d() default "";
    /** The object value of the element, if type == OBJECT */
    java.lang.Class<?> o() default java.lang.Object.class;
}
