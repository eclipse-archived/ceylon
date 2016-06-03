package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for methods of annotation types which although encoded as a String
 * are actually a metamodel declaration reference.
 * <pre>
 * @interface Foo$annotation {
 *
 *     @.com.redhat.ceylon.compiler.java.metadata.DeclarationReference
 *     public abstract .java.lang.String mmr();
 * }
 * </pre>
 * @author tom
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface DeclarationReference {
}
