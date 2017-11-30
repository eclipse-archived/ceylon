/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.eclipse.ceylon.compiler.java.test.annotations;

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