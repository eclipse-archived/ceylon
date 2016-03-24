package com.redhat.ceylon.common;

public enum Platform {
    JVM(Backend.Java), NODE(Backend.JavaScript), BROWSER(Backend.JavaScript);
    
    public final Backend backend;
    
    Platform(Backend backend) {
        this.backend = backend;
    }
    
}
