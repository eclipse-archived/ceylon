package com.redhat.ceylon.model.typechecker.model;

public class DeclarationFlags {
    
    public static final int SHARED = 1;
    public static final int FORMAL = 1<<1;
    public static final int ACTUAL = 1<<2;
    public static final int DEFAULT = 1<<3;
    public static final int ANNOTATION = 1<<4;
    public static final int DEPRECATED = 1<<5;
    public static final int DYNAMIC = 1<<6;

    public static final int PROTECTED = 1<<8;
    public static final int PACKAGE = 1<<9;
    public static final int STATIC = 1<<10;
    public static final int DROPPED = 1<<30;

    public static class TypedDeclarationFlags extends DeclarationFlags {
        public static final int UNCHECKED_NULL = 1<<11;
        public static final int UNBOXED_KNOWN = 1<<12;
        public static final int UNBOXED = 1<<13;
        public static final int TYPE_ERASED = 1<<14;
        public static final int UNTRUSTED_TYPE = 1<<15;
        public static final int DYNAMICALLY_TYPED = 1<<16;
    }
    
    public static class FunctionOrValueFlags extends TypedDeclarationFlags {
        public static final int CAPTURED = 1<<17;
        public static final int SHORTCUT_REFINEMENT = 1<<18;
        public static final int OVERLOADED = 1<<19;
        public static final int ABSTRACTION = 1<<20;
        public static final int IMPLEMENTED = 1<<21;
        public static final int SMALL = 1<<29;
    }

    public static class FunctionFlags extends FunctionOrValueFlags {
        public static final int VOID = 1<<22;
        public static final int DEFERRED = 1<<23;
        public static final int NO_NAME = 1<<24;
        public static final int VARIADIC = 1<<26;
    }

    public static class ValueFlags extends FunctionOrValueFlags {
        public static final int VARIABLE = 1<<22;
        public static final int TRANSIENT = 1<<23;
        public static final int LATE = 1<<24;
        public static final int ENUM_VALUE = 1<<25;
        public static final int SPECIFIED_IN_FOR_ELSE = 1<<26;
        public static final int INFERRED = 1<<27;
        public static final int SELF_CAPTURED = 1<<28;
    }
    
    public static class ConstructorFlags extends DeclarationFlags {
        public static final int ABSTRACT = 1<<13;
        public static final int OVERLOADED = 1<<19;
        public static final int ABSTRACTION = 1<<20;
    }
    
    public static class ClassFlags extends DeclarationFlags {
        public static final int CONSTRUCTORS = 1<<11;
        public static final int ENUMERATED = 1<<12;
        public static final int ABSTRACT = 1<<13;
        public static final int FINAL = 1<<14;
        public static final int SERIALIZABLE = 1<<15;
        public static final int ANONYMOUS = 1<<16;
        public static final int JAVA_ENUM = 1<<17;
        public static final int VALUE_CONSTRUCTOR = 1<<18;
        public static final int OVERLOADED = 1<<19;
        public static final int ABSTRACTION = 1<<20;
        public static final int NO_NAME = 1<<24;
    }
}
