package org.eclipse.ceylon.compiler.java.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * Marks a JUnit 4 test class that should not be run concurrently with 
 * other tests by the {@link ConcurrentScheduler}
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RunSingleThreaded {

}
