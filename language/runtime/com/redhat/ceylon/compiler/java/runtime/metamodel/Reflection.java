package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.reflect.Method;
import java.util.Arrays;

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
    
}
