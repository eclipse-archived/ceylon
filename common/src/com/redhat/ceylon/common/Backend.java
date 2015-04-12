package com.redhat.ceylon.common;

public enum Backend {
    Java("jvm"),
    JavaScript("js"),
    Documentation("doc");
    
    public final String compilerAnnotation;
    
    Backend(String compilerAnnotation) {
        this.compilerAnnotation = compilerAnnotation;
    }
}