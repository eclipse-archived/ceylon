package com.redhat.ceylon.common;

public interface BackendSupport {
    boolean supportsBackend(String backend);
    
    public static final BackendSupport JAVA = new BackendSupport() {
        @Override
        public boolean supportsBackend(String backend) {
            return Backend.Java.nativeAnnotation.equals(backend);
        }

        @Override
        public String toString() {
            return "BackendSupport for " + Backend.Java;
        }
    };
    
    public static final BackendSupport JAVASCRIPT = new BackendSupport() {
        @Override
        public boolean supportsBackend(String backend) {
            return Backend.JavaScript.nativeAnnotation.equals(backend);
        }

        @Override
        public String toString() {
            return "BackendSupport for " + Backend.JavaScript;
        }
    };
    
    public static final BackendSupport HEADER = new BackendSupport() {
        @Override
        public boolean supportsBackend(String backend) {
            return Backend.None.nativeAnnotation.equals(backend);
        }

        @Override
        public String toString() {
            return "BackendSupport for " + Backend.None;
        }
    };
}
