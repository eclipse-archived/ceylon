package com.redhat.ceylon.common;

public enum Backend {
    Java("jvm"),
    JavaScript("js");
    
    public final String nativeAnnotation;
    
    Backend(String compilerAnnotation) {
        this.nativeAnnotation = compilerAnnotation;
    }
}