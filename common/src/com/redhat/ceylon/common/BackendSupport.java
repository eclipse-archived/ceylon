package com.redhat.ceylon.common;

public interface BackendSupport {
    boolean supportsBackend(Backend backend);
    
    public static final BackendSupport JAVA = new BackendSupport() {
        @Override
        public boolean supportsBackend(Backend backend) {
            return backend == Backend.Java;
        }

        @Override
        public String toString() {
            return "BackendSupport for " + Backend.Java;
        }
    };
    
    public static final BackendSupport JAVASCRIPT = new BackendSupport() {
        @Override
        public boolean supportsBackend(Backend backend) {
            return backend == Backend.JavaScript;
        }

        @Override
        public String toString() {
            return "BackendSupport for " + Backend.JavaScript;
        }
    };
    
    public static final BackendSupport HEADER = new BackendSupport() {
        @Override
        public boolean supportsBackend(Backend backend) {
            return backend == Backend.None;
        }

        @Override
        public String toString() {
            return "BackendSupport for " + Backend.None;
        }
    };
}
