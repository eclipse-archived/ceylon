package com.redhat.ceylon.compiler.java.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// unambiguous
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
@interface TypeTarget {}

@Target(ElementType.CONSTRUCTOR) 
@Retention(RetentionPolicy.RUNTIME)
@interface ConstructorTarget {}

@Target(ElementType.FIELD) 
@Retention(RetentionPolicy.RUNTIME)
@interface FieldTarget {}

@Target(ElementType.PARAMETER) 
@Retention(RetentionPolicy.RUNTIME)
@interface ParameterTarget {}

@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.RUNTIME)
@interface MethodTarget {}

@Target(ElementType.ANNOTATION_TYPE) 
@Retention(RetentionPolicy.RUNTIME)
@interface AnnotationTypeTarget {}

@Target(ElementType.LOCAL_VARIABLE) 
@Retention(RetentionPolicy.RUNTIME)
@interface LocalVariableTarget {}

@Target(ElementType.PACKAGE) 
@Retention(RetentionPolicy.RUNTIME)
@interface PackageTarget {}


// potentially ambiguous...
// ... when applied to a Ceylon class
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR}) 
@Retention(RetentionPolicy.RUNTIME)
@interface TypeOrConstructorTarget {}

//... when applied to a Ceylon singleton constructor
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
@interface ConstructorOrFieldOrMethodTarget {}

// ... when applied to a Ceylon reference value (field, or getter or setter)
@Target({ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
@interface FieldOrMethodTarget {}

// ... when applied to a Ceylon shared/captured class initializer parameter
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
@interface ParameterOrFieldOrMethodTarget {}

// ... when applied to a Ceylon class initializer parameter
@Target({ElementType.PARAMETER, ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME)
@interface ParameterOrFieldTarget {}

// ... when applied to an Ceylon annotation class
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE}) 
@Retention(RetentionPolicy.RUNTIME)
@interface TypeOrAnnotationTypeTarget {}

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME)
@interface Bug2160 {}