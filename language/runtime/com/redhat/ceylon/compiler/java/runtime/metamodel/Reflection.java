package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

class Reflection {

    private Reflection() {} 
    
    /**
     * <p>Returns the method declared on the given class 
     * with the given name and the given parameter types,
     * or null if no such method exists.
     * If the method exists it will have been made 
     * {@linkplain AccessibleObject#setAccessible(boolean) accessible}.</p>
     * 
     * <p>Unlike {@link Class#getDeclaredMethod(String, Class...)} 
     * this method will find non-{@code public} methods.</p> 
     */
    static Method getDeclaredMethod(Class<?> cls, String name, 
            java.lang.Class<?>... parameterTypes) {
        for (Method method : cls.getDeclaredMethods()) {
            if (method.getName() == name
                    && !method.isSynthetic()
                    && !method.isBridge()
                    && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }
    
    /**
     * <p>Returns the {@code void} unary method 
     * with the given name declared on the given class,
     * or null if no such method exists.
     * If the method exists it will have been made 
     * {@linkplain AccessibleObject#setAccessible(boolean) accessible}.</p>
     * 
     * <p>Unlike {@link Class#getDeclaredMethod(String, Class...)} 
     * this method will find non-{@code public} methods.</p> 
     */
    static Method getDeclaredSetter(Class<?> cls, String setterName) {
        for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
            if (method.getName().equals(setterName)
                    && !method.isSynthetic()
                    && !method.isBridge()
                    && method.getReturnType() == void.class
                    && method.getParameterTypes().length == 1) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }
    
    /**
     * <p>Returns the non-{@code void} nullary method 
     * with the given name declared on the given class,  
     * or null if no such method exists. 
     * If the method exists it will have been made 
     * {@linkplain AccessibleObject#setAccessible(boolean) accessible}.</p>
     * 
     * <p>Unlike {@link Class#getDeclaredMethod(String, Class...)} 
     * this method will find non-{@code public} methods.</p> 
     */
    static Method getDeclaredGetter(Class<?> cls, String getterName) {
        for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
            if (method.getName().equals(getterName)
                    && !method.isSynthetic()
                    && !method.isBridge()
                    && method.getReturnType() != void.class
                    && method.getParameterTypes().length == 0) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    static java.lang.reflect.Constructor<?> findConstructor(Class<?> javaClass) {
        // How to find the right Method, just go for the one with the longest parameter list?
        // OR go via the Method in AppliedFunction?
        java.lang.reflect.Constructor<?> best = null;
        int numBestParams = -1;
        int numBest = 0;
        for (java.lang.reflect.Constructor<?> meth : javaClass.getDeclaredConstructors()) {
            if (meth.isSynthetic()
                    || meth.getAnnotation(Ignore.class) != null) {
                continue;
            }
            
            Class<?>[] parameterTypes = meth.getParameterTypes();
            if (parameterTypes.length > numBestParams) {
                best = meth;
                numBestParams = parameterTypes.length;
                numBest = 1;
            } else if (parameterTypes.length == numBestParams) {
                numBest++;
            }
        }
        if (best == null) {
            throw new RuntimeException("Couldn't find method " + javaClass);
        }
        if (numBest > 1) {
            throw new RuntimeException("Method arity ambiguity " + javaClass);
        }
        return best;
    }

}
