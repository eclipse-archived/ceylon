package com.redhat.ceylon.compiler.typechecker.model;

/**
 * An argument to an annotation class instantiation that is a 'literal'.
 * Despite the name this is used for the top level objects {@code true}
 * and {@code false} as well as Number, Float, Character and 
 * String literals.
 */
public class LiteralAnnotationArgument extends AnnotationArgument {
}