package com.redhat.ceylon.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Backend {
    public static final Backend None;
    public static final Backend Java;
    public static final Backend JavaScript;
    
    private static final Set<Backend> backends;
    
    static {
        backends = new HashSet<Backend>();
        None = registerBackend("");
        Java = registerBackend("jvm");
        JavaScript = registerBackend("js");
    }
    
    public final String nativeAnnotation;
    
    private Backend(String nativeAnnotation) {
        this.nativeAnnotation = nativeAnnotation;
    }
    
    public static Backend registerBackend(String backend) {
        Backend b = fromAnnotation(backend);
        if (b == null) {
            b = new Backend(backend);
            backends.add(b);
        }
        return b;
    }
    
    public static Set<Backend> getRegisteredBackends() {
        return Collections.unmodifiableSet(backends);
    }

    public static boolean validAnnotation(String backend) {
        return fromAnnotation(backend) != null;
    }
    
    public static Backend fromAnnotation(String backend) {
        if (backend != null) {
            if (backend.isEmpty()) {
                return None;
            }
            for (Backend b : backends) {
                if (b.nativeAnnotation.equals(backend)) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return nativeAnnotation.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof Backend) {
            return nativeAnnotation.equals(((Backend)that).nativeAnnotation);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return nativeAnnotation.isEmpty() ? "native" : "native(" + nativeAnnotation + ")";
    }
}
