package com.redhat.ceylon.common;

public enum Backend {
    None(""),
    Java("jvm"),
    JavaScript("js");
    
    public final String nativeAnnotation;
    
    Backend(String nativeAnnotation) {
        this.nativeAnnotation = nativeAnnotation;
    }
    
    public static boolean validAnnotation(String backend) {
        return fromAnnotation(backend) != null;
    }
    
    public static Backend fromAnnotation(String backend) {
        for (Backend b : Backend.values()) {
            if (b.nativeAnnotation.equals(backend)) {
                return b;
            }
        }
        return null;
    }
    
}