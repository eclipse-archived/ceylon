package com.redhat.ceylon.compiler.java.test.interop;

public interface JavaInterface<B,I> {
    boolean booleanMethod(boolean b);
    Boolean boxedBooleanMethod(Boolean b);
    ceylon.language.Boolean ceylonBooleanMethod(ceylon.language.Boolean b);

    B classTypeParamMethodB(B t);

    long longMethod(long i);
    Long boxedLongMethod(Long i);
    ceylon.language.Integer ceylonIntegerMethod(ceylon.language.Integer i);

    I classTypeParamMethodI(I t);

    <M> M methodTypeParamMethod(M m); 
}
