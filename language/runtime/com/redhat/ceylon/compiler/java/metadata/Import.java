package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Import {
	public boolean export() default false;
	public boolean optional() default false;
	public String name() default "";
	public String version() default "";
    /** The import native backends. */
    public String[] nativeBackends() default {};
}
