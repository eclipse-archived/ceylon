package com.redhat.ceylon.compiler.java.test.interop;

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
    //TODO java.lang.Class<? extends java.lang.Exception>[] classWithBound();
    java.lang.Class<?>[] classes();
    //TODO java.lang.Class<? extends java.lang.Exception>[] classesWithBound();
}

@interface JavaAnnotationAnnotation {

    JavaAnnotationEnum annotation();
    //JavaAnnotationEnum[] annotations();
}
