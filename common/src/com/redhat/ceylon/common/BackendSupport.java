package com.redhat.ceylon.common;

public interface BackendSupport {
    boolean supportsBackend(Backend backend);
    
    public static final BackendSupport JAVA = new BackendSupport() {
        @Override
        public boolean supportsBackend(Backend backend) {
            return backend == Backend.Java;
        }
    };
    
    public static final BackendSupport JAVASCRIPT = new BackendSupport() {
        @Override
        public boolean supportsBackend(Backend backend) {
            return backend == Backend.JavaScript;
        }
    };
}
