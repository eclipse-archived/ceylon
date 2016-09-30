package com.redhat.ceylon.model.typechecker.model;

/**
 * Flags for {@link Declaration#flags}. Don't mess it up.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class DeclarationFlags {
    
    public static final int SHARED         = 1 << 0;
    public static final int FORMAL         = 1 << 1;
    public static final int ACTUAL         = 1 << 2;
    public static final int DEFAULT        = 1 << 3;
    public static final int ANNOTATION     = 1 << 4;
    public static final int DEPRECATED     = 1 << 5;
    public static final int DYNAMIC        = 1 << 6;
                                           
    public static final int PROTECTED      = 1 << 7;
    public static final int PACKAGE        = 1 << 8;
    public static final int STATIC         = 1 << 9;
    public static final int DROPPED        = 1 << 10;
    public static final int COERCION_POINT = 1 << 11;
    private static final int LAST          = 11; // KEEP THIS IN SYNC WITH LAST SHIFT

    public static class TypedDeclarationFlags extends DeclarationFlags {
        public static final int UNCHECKED_NULL    = 1 << DeclarationFlags.LAST + 1;
        public static final int UNBOXED_KNOWN     = 1 << DeclarationFlags.LAST + 2;
        public static final int UNBOXED           = 1 << DeclarationFlags.LAST + 3;
        public static final int TYPE_ERASED       = 1 << DeclarationFlags.LAST + 4;
        public static final int UNTRUSTED_TYPE    = 1 << DeclarationFlags.LAST + 5;
        public static final int DYNAMICALLY_TYPED = 1 << DeclarationFlags.LAST + 6;
        private static final int LAST             = DeclarationFlags.LAST + 6; // KEEP THIS IN SYNC WITH LAST SHIFT
    }
    
    public static class FunctionOrValueFlags extends TypedDeclarationFlags {
        public static final int CAPTURED            = 1 << TypedDeclarationFlags.LAST + 1;
        public static final int SHORTCUT_REFINEMENT = 1 << TypedDeclarationFlags.LAST + 2;
        public static final int OVERLOADED          = 1 << TypedDeclarationFlags.LAST + 3;
        public static final int ABSTRACTION         = 1 << TypedDeclarationFlags.LAST + 4;
        public static final int IMPLEMENTED         = 1 << TypedDeclarationFlags.LAST + 5;
        public static final int SMALL               = 1 << TypedDeclarationFlags.LAST + 6;
        private static final int LAST               = TypedDeclarationFlags.LAST + 6; // KEEP THIS IN SYNC WITH LAST SHIFT
    }

    public static class FunctionFlags extends FunctionOrValueFlags {
        public static final int VOID     = 1 << FunctionOrValueFlags.LAST + 1;
        public static final int DEFERRED = 1 << FunctionOrValueFlags.LAST + 2;
        public static final int NO_NAME  = 1 << FunctionOrValueFlags.LAST + 3;
        public static final int VARIADIC = 1 << FunctionOrValueFlags.LAST + 4;
    }

    public static class ValueFlags extends FunctionOrValueFlags {
        public static final int VARIABLE              = 1 << FunctionOrValueFlags.LAST + 1;
        public static final int TRANSIENT             = 1 << FunctionOrValueFlags.LAST + 2;
        public static final int LATE                  = 1 << FunctionOrValueFlags.LAST + 3;
        public static final int ENUM_VALUE            = 1 << FunctionOrValueFlags.LAST + 4;
        public static final int SPECIFIED_IN_FOR_ELSE = 1 << FunctionOrValueFlags.LAST + 5;
        public static final int INFERRED              = 1 << FunctionOrValueFlags.LAST + 6;
        public static final int SELF_CAPTURED         = 1 << FunctionOrValueFlags.LAST + 7;
    }
    
    public static class ConstructorFlags extends DeclarationFlags {
        public static final int ABSTRACT    = 1 << DeclarationFlags.LAST + 1;
        public static final int OVERLOADED  = 1 << DeclarationFlags.LAST + 2;
        public static final int ABSTRACTION = 1 << DeclarationFlags.LAST + 3;
    }
    
    public static class ClassFlags extends DeclarationFlags {
        public static final int CONSTRUCTORS      = 1 << DeclarationFlags.LAST + 1;
        public static final int ENUMERATED        = 1 << DeclarationFlags.LAST + 2;
        public static final int ABSTRACT          = 1 << DeclarationFlags.LAST + 3;
        public static final int FINAL             = 1 << DeclarationFlags.LAST + 4;
        public static final int SERIALIZABLE      = 1 << DeclarationFlags.LAST + 5;
        public static final int ANONYMOUS         = 1 << DeclarationFlags.LAST + 6;
        public static final int JAVA_ENUM         = 1 << DeclarationFlags.LAST + 7;
        public static final int VALUE_CONSTRUCTOR = 1 << DeclarationFlags.LAST + 8;
        public static final int OVERLOADED        = 1 << DeclarationFlags.LAST + 9;
        public static final int ABSTRACTION       = 1 << DeclarationFlags.LAST + 10;
        public static final int NO_NAME           = 1 << DeclarationFlags.LAST + 11;
    }
}
