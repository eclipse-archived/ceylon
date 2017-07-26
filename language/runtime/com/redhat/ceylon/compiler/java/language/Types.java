package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.metamodel.decl.ClassOrInterfaceDeclarationImpl;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Array;
import ceylon.language.meta.declaration.ClassOrInterfaceDeclaration;
import ceylon.language.meta.model.ClassOrInterface;

@Ceylon(major = 8) 
@com.redhat.ceylon.compiler.java.metadata.Class(constructors=true)
public final class Types {

    /**
     * A {@link java.lang.Class} object representing 
     * the given representing the concrete type of 
     * the given instance.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    @TypeInfo("java.lang::Class<out T>")
    @TypeParameters({@TypeParameter(
            value = "T",
            satisfies = "ceylon.language::Object")})
    @SuppressWarnings("unchecked")
    public static <T> java.lang.Class<? extends T> 
    classForInstance(@Ignore TypeDescriptor $reifiedT,
            @Name("instance")
            @com.redhat.ceylon.common.NonNull
            T instance) {
        return (Class<? extends T>) instance.getClass();
    }
    
    /**
     * A {@link java.lang.Class} object 
     * representing the given type {@link T}.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    @TypeInfo("java.lang::Class<T>")
    @TypeParameters({@TypeParameter(
            value = "T",
            satisfies = "ceylon.language::Object")})
    @SuppressWarnings("unchecked")
    public static <T> java.lang.Class<T> 
    classForType(@Ignore TypeDescriptor $reifiedT) {
        java.lang.Class<T> result = 
                (java.lang.Class<T>)
                    Metamodel.getJavaClass($reifiedT);
        if (result != null) {
            return result;
        }
        throw new ceylon.language.AssertionError("unsupported type: '" 
                + $reifiedT 
                + "' cannot be represented by a java.lang.Class");
    }
    
    /**
     * A {@link java.lang.Class} object representing the Java
     * annotation type corresponding to the given Ceylon
     * annotation class {@link T}.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    @TypeInfo("java.lang::Class<out T>")
    @TypeParameters({@TypeParameter(
            value = "T",
            satisfies = "ceylon.language::Annotation")})
    @SuppressWarnings("unchecked")
    public static <T extends java.lang.annotation.Annotation> Class<T>
    classForAnnotationType(@Ignore TypeDescriptor $reifiedT) {
        if ($reifiedT instanceof TypeDescriptor.Class) {
            TypeDescriptor.Class klass = 
                    (TypeDescriptor.Class) $reifiedT;
            if (klass.getTypeArguments().length > 0)
                throw new RuntimeException("given type has type arguments");
            try {
                Class<?> c = klass.getKlass();
                String name = c.getName() + "$annotation$";
                return (Class<T>) 
                        Class.forName(name, true, c.getClassLoader());
            }
            catch (ClassNotFoundException e) {}
        } 
        throw new ceylon.language.AssertionError("unsupported type");
    }

    /**
     * A {@link java.lang.Class} object representing 
     * the given Ceylon 
     * <code>ClassOrInterfaceDeclaration</code>.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    @TypeInfo("java.lang::Class<out ceylon.language::Object>")
    public static java.lang.Class<? extends java.lang.Object> 
    classForDeclaration(
            @Name("declaration")
            @com.redhat.ceylon.common.NonNull
            ClassOrInterfaceDeclaration decl) {
        if (decl instanceof ClassOrInterfaceDeclarationImpl) {
            ClassOrInterfaceDeclarationImpl ci = 
                    (ClassOrInterfaceDeclarationImpl) decl;
            return erase(ci.getJavaClass());
        }
        throw new ceylon.language.AssertionError("Unsupported declaration type: "+decl);
    }

    /**
     * A {@link java.lang.Class} object representing 
     * the given Ceylon <code>ClassOrInterface</code>.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    @TypeInfo("java.lang::Class<out T>")
    @TypeParameters({@TypeParameter(
            value = "T",
            satisfies = "ceylon.language::Object")})
    @SuppressWarnings("unchecked")
    public static <T> java.lang.Class<? extends T> 
    classForModel(@Ignore TypeDescriptor $reifiedT,
            @Name("model")
            @com.redhat.ceylon.common.NonNull
            ClassOrInterface<? extends T> model) {
        ClassOrInterfaceDeclaration decl = model.getDeclaration();
        if (decl instanceof ClassOrInterfaceDeclarationImpl) {
            ClassOrInterfaceDeclarationImpl ci = 
                    (ClassOrInterfaceDeclarationImpl) decl;
            return (Class<? extends T>) erase(ci.getJavaClass());
        }
        throw new ceylon.language.AssertionError("Unsupported declaration type: "+decl);
    }

    @SuppressWarnings("unchecked")
    private static <T> java.lang.Class<? extends T>
    erase(java.lang.Class<? extends T> klass){
      // dirty but keeps the logic in one place
      return (Class<? extends T>)
              TypeDescriptor.klass(klass)
                  .getArrayElementClass();
    }
    
    /**
     * A Ceylon <code>String</code> with the 
     * codepoints of the given <code>char[]</code> 
     * array.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    @TypeInfo("ceylon.language::String")
    public static java.lang.String codepoints(
            @Name("array")
            @com.redhat.ceylon.common.NonNull
            final char[] array) {
        return new java.lang.String(array);
    }
    
    /**
     * A <code>char[]</code> array with the 
     * characters of the given Ceylon <code>String</code>.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    public static char[] charArray(
            @Name("string")
            @TypeInfo("ceylon.language::String")
            @com.redhat.ceylon.common.NonNull
            final java.lang.String string) {
        return string.toCharArray();
    }
    
    /**
     * The {@link java.lang.String} underlying the 
     * given Ceylon <code>String</code>.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    @TypeInfo("java.lang::String")
    public static java.lang.String nativeString(
            @Name("string")
            @TypeInfo("ceylon.language::String")
            @com.redhat.ceylon.common.NonNull
            final java.lang.String string) {
        return string;
    }
    
    /**
     * The <code>long[]</code> array underlying the 
     * given Ceylon <code>Array&lt;Integer&gt;</code>.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    public static long[] longArray(
            @Name("array")
            @TypeInfo("ceylon.language::Array<ceylon.language::Integer>")
            @com.redhat.ceylon.common.NonNull
            final Array<ceylon.language.Integer> array) {
        return (long[]) array.toArray();
    }
    
    /**
     * The <code>double[]</code> array underlying the 
     * given Ceylon <code>Array&lt;Float&gt;</code>.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    public static double[] doubleArray(
            @Name("array")
            @TypeInfo("ceylon.language::Array<ceylon.language::Float>")
            @com.redhat.ceylon.common.NonNull
            final Array<ceylon.language.Float> array) {
        return (double[]) array.toArray();
    }
    
    /**
     * The <code>byte[]</code> array underlying the 
     * given Ceylon <code>Array&lt;Byte&gt;</code>.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    public static byte[] byteArray(
            @Name("array")
            @TypeInfo("ceylon.language::Array<ceylon.language::Byte>")
            @com.redhat.ceylon.common.NonNull
            final Array<ceylon.language.Byte> array) {
        return (byte[]) array.toArray();
    }
    
    /**
     * The <code>boolean[]</code> array underlying the 
     * given Ceylon <code>Array&lt;Boolean&gt;</code>.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    public static boolean[] booleanArray(
            @Name("array")
            @TypeInfo("ceylon.language::Array<ceylon.language::Boolean>")
            @com.redhat.ceylon.common.NonNull
            final Array<ceylon.language.Boolean> array) {
        return (boolean[]) array.toArray();
    }
    
    /**
     * The <code>Object[]</code> array underlying the 
     * given Ceylon <code>Array&lt;Anything&gt;</code>.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    public static Object[] objectArray(
            @Name("array")
            @TypeInfo("ceylon.language::Array<ceylon.language::Anything>")
            @com.redhat.ceylon.common.NonNull
            final Array<Object> array) {
        return (Object[]) array.toArray();
    }
    
    /**
     * The stack trace information for the given 
     * exception as a Ceylon <code>Array</code> of 
     * {@link StackTraceElement}s, or an empty array 
     * if no stack trace information is available. The 
     * first element of the sequence is the top of the 
     * stack, that is, the most deeply nested stack 
     * frame. This is usually the stack frame in which 
     * the given exception was created and thrown.
     */
    @ceylon.language.StaticAnnotation$annotation$
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.common.NonNull
    public static Array<StackTraceElement> stackTrace(
            @TypeInfo("ceylon.language::Throwable")
            @Name("throwable")
            @com.redhat.ceylon.common.NonNull
            Throwable throwable) {
        return Array.stackTrace(throwable.getStackTrace());
    }
    
}
