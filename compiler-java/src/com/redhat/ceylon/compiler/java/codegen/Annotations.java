package com.redhat.ceylon.compiler.java.codegen;

public class Annotations {

    public static final int NONE = 0;
    
    public static final int IGNORE = 1<<0;
    public static final int MODEL = 1<<1;
    public static final int USER = 1<<2;
    
    public static final int MODEL_AND_USER = MODEL | USER;
    
    private Annotations() {}
    
    /** determine whether to include model annotations */
    public static boolean includeModel(int flags) {
        return (flags & MODEL) != 0;
    }
    
    /** determine whether to include user annotations */
    public static boolean includeUser(int flags) {
        return (flags & USER) != 0;
    }
    
    /** determine whether to include the {@Ignore} annotation */
    public static boolean includeIgnore(int flags) {
        return (flags & IGNORE) != 0;
    }
    /** Clears the {@link #MODEL} bit and sets the {@link #IGNORE} bit */
    public static int ignore(int annotationFlags) {
        return (annotationFlags & ~MODEL) | IGNORE;
    }
    /** Clears the {@link #MODEL} bit and sets the {@link #IGNORE} bit */
    public static int noUserOrModel(int annotationFlags) {
        return (annotationFlags & ~(MODEL|USER));
    }
    
}
