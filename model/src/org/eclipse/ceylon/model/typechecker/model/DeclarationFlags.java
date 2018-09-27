/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

/**
 * Flags for {@link Declaration#flags}. Don't mess it up.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class DeclarationFlags {
    
    public static final long SHARED         = 1L << 0;
    public static final long FORMAL         = 1L << 1;
    public static final long ACTUAL         = 1L << 2;
    public static final long DEFAULT        = 1L << 3;
    public static final long ANNOTATION     = 1L << 4;
    public static final long DEPRECATED     = 1L << 5;
    public static final long DYNAMIC        = 1L << 6;
                                           
    public static final long PROTECTED      = 1L << 7;
    public static final long PACKAGE        = 1L << 8;
    public static final long STATIC         = 1L << 9;
    public static final long DROPPED        = 1L << 10;
    public static final long COERCION_POINT = 1L << 11;
    private static final int LAST          = 11; // KEEP THIS IN SYNC WITH LAST SHIFT

    public static class TypedDeclarationFlags extends DeclarationFlags {
        public static final long UNCHECKED_NULL    = 1L << DeclarationFlags.LAST + 1;
        public static final long UNBOXED_KNOWN     = 1L << DeclarationFlags.LAST + 2;
        public static final long UNBOXED           = 1L << DeclarationFlags.LAST + 3;
        public static final long TYPE_ERASED       = 1L << DeclarationFlags.LAST + 4;
        public static final long UNTRUSTED_TYPE    = 1L << DeclarationFlags.LAST + 5;
        public static final long UNTRUSTED_KNOWN   = 1L << DeclarationFlags.LAST + 6;
        public static final long DYNAMICALLY_TYPED = 1L << DeclarationFlags.LAST + 7;
        private static final int LAST             = DeclarationFlags.LAST + 7; // KEEP THIS IN SYNC WITH LAST SHIFT
    }
    
    public static class FunctionOrValueFlags extends TypedDeclarationFlags {
        public static final long CAPTURED            = 1L << TypedDeclarationFlags.LAST + 1;
        public static final long JS_CAPTURED         = 1L << TypedDeclarationFlags.LAST + 2;
        public static final long SHORTCUT_REFINEMENT = 1L << TypedDeclarationFlags.LAST + 3;
        public static final long OVERLOADED          = 1L << TypedDeclarationFlags.LAST + 4;
        public static final long ABSTRACTION         = 1L << TypedDeclarationFlags.LAST + 5;
        public static final long IMPLEMENTED         = 1L << TypedDeclarationFlags.LAST + 6;
        public static final long SMALL               = 1L << TypedDeclarationFlags.LAST + 7;
        public static final long JAVA_NATIVE         = 1L << TypedDeclarationFlags.LAST + 8;
        public static final long CONSTRUCTOR         = 1L << TypedDeclarationFlags.LAST + 9;
        private static final int LAST               = TypedDeclarationFlags.LAST + 10; // KEEP THIS IN SYNC WITH LAST SHIFT
    }

    public static class FunctionFlags extends FunctionOrValueFlags {
        public static final long VOID     = 1L << FunctionOrValueFlags.LAST + 1;
        public static final long DEFERRED = 1L << FunctionOrValueFlags.LAST + 2;
        public static final long NO_NAME  = 1L << FunctionOrValueFlags.LAST + 3;
        public static final long VARIADIC = 1L << FunctionOrValueFlags.LAST + 4;
    }

    public static class ValueFlags extends FunctionOrValueFlags {
        public static final long VARIABLE              = 1L << FunctionOrValueFlags.LAST + 1;
        public static final long TRANSIENT             = 1L << FunctionOrValueFlags.LAST + 2;
        public static final long LATE                  = 1L << FunctionOrValueFlags.LAST + 3;
        public static final long ENUM_VALUE            = 1L << FunctionOrValueFlags.LAST + 4;
        public static final long SPECIFIED_IN_FOR_ELSE = 1L << FunctionOrValueFlags.LAST + 5;
        public static final long INFERRED              = 1L << FunctionOrValueFlags.LAST + 6;
        public static final long SELF_CAPTURED         = 1L << FunctionOrValueFlags.LAST + 7;
    }
    
    public static class ConstructorFlags extends DeclarationFlags {
        public static final long ABSTRACT    = 1L << DeclarationFlags.LAST + 1;
        public static final long OVERLOADED  = 1L << DeclarationFlags.LAST + 2;
        public static final long ABSTRACTION = 1L << DeclarationFlags.LAST + 3;
    }
    
    public static class ClassFlags extends DeclarationFlags {
        public static final long CONSTRUCTORS      = 1L << DeclarationFlags.LAST + 1;
        public static final long ENUMERATED        = 1L << DeclarationFlags.LAST + 2;
        public static final long ABSTRACT          = 1L << DeclarationFlags.LAST + 3;
        public static final long FINAL             = 1L << DeclarationFlags.LAST + 4;
        public static final long SERIALIZABLE      = 1L << DeclarationFlags.LAST + 5;
        public static final long ANONYMOUS         = 1L << DeclarationFlags.LAST + 6;
        public static final long JAVA_ENUM         = 1L << DeclarationFlags.LAST + 7;
        public static final long OVERLOADED        = 1L << DeclarationFlags.LAST + 9;
        public static final long ABSTRACTION       = 1L << DeclarationFlags.LAST + 10;
        public static final long NO_NAME           = 1L << DeclarationFlags.LAST + 11;
    }
}
