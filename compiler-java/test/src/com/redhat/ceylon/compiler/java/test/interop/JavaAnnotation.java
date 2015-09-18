package com.redhat.ceylon.compiler.java.test.interop;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@interface JavaAnnotationPrimitives {
    boolean b();
    byte o();
    short s();
    int i() default 1;
    long l();
    float f();
    double d();
    String str();
    
    boolean[] ba();
    byte[] oa();
    short[] sa();
    int[] ia() default {1, 2};
    long[] la();
    float[] fa();
    double[] da();
    String[] stra() default {"a"};
}

@interface JavaAnnotationEnum {
    java.lang.Thread.State threadState();
    java.lang.Thread.State[] threadStates();
}

@interface JavaAnnotationClass {
    java.lang.Class<?> clas();
    java.lang.Class classRaw();
    java.lang.Class<? extends java.lang.Throwable> classWithBound();
    java.lang.Class<java.lang.String> classExact();
    java.lang.Class<?>[] classes();
    java.lang.Class[] classesRaw();
    java.lang.Class<? extends java.lang.Throwable>[] classesWithBound();
    java.lang.Class<java.lang.String>[] classesExact();
}

@interface JavaAnnotationAnnotation {

    JavaAnnotationEnum annotation();
    JavaAnnotationEnum[] annotations();
}

@interface JAVAAnnotationAcronym {}
@interface javaAnnotationLowercase {}

@interface JavaAnnotationDefaultTarget {}
@Target({})
@interface JavaAnnotationNoTarget {}
@Target({
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.FIELD,
    ElementType.LOCAL_VARIABLE,
    ElementType.METHOD,
    ElementType.PACKAGE,
    ElementType.PARAMETER,
    ElementType.TYPE,
})
@interface JavaAnnotationOnEveryTarget {}

@Target({ElementType.TYPE})
@interface JavaAnnotationTypeTarget {}
@Target({ElementType.CONSTRUCTOR})
@interface JavaAnnotationCtorTarget {}
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR})
@interface JavaAnnotationTypeCtorTarget {}

@interface JavaAnnotationClass2 {
    java.lang.Class<?> clas();
}