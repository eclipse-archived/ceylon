package com.redhat.ceylon.compiler.java.metadata;

public @interface Import {
	public boolean export() default false;
	public boolean optional() default false;
	public String name() default "";
	public String version() default "";
}
