package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for methods of annotation types, encoded as a java.lang.Class, 
 * that are actually a reference to an anonymous class that's a case of an enumerated type.
 * This is not used for booleans though. 
 * <pre>
 * @interface Foo$annotation {
 *     @.com.redhat.ceylon.compiler.java.metadata.EnumerationReference
 *     public abstract .java.lang.Class<? extends Bar> bar();
 * }
 * </pre>
 * @author tom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnumerationReference {
}
