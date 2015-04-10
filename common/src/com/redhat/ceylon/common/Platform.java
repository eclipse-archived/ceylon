package com.redhat.ceylon.common;

public enum Platform {
    JVM(Backend.JVM), NODE(Backend.JS), BROWSER(Backend.JS);
    
    public final Backend backend;
    
    Platform(Backend backend) {
        this.backend = backend;
    }
    
}
