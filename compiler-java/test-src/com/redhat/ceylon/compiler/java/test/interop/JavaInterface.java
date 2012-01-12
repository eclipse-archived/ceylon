package com.redhat.ceylon.compiler.java.test.interop;

public interface JavaInterface<T> {
    boolean booleanMethod(boolean b);
    Boolean boxedBooleanMethod(Boolean b);
    ceylon.language.Boolean ceylonBooleanMethod(ceylon.language.Boolean b);
    
    T classTypeParamMethod(T t);
    
    <M> M methodTypeParamMethod(M m); 
}
