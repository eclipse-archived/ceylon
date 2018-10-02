/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.model.loader.NamingBase;
import org.eclipse.ceylon.model.typechecker.model.ClassAlias;

public class Reflection {

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
            if (method.getName().equals(name)
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
    public static Method getDeclaredSetter(Class<?> cls, String setterName) {
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
    public static Method getDeclaredGetter(Class<?> cls, String getterName) {
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
    
    public static java.lang.reflect.Field getDeclaredField(Class<?> cls, String fieldName) {
        for (java.lang.reflect.Field field : cls.getDeclaredFields()) {
            if (field.getName().equals(fieldName)
                    && !field.isSynthetic()) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    public static java.lang.reflect.Method findClassAliasInstantiator(Class<?> javaClass, ClassAlias container) {
        Class<?> searchClass;
        if (javaClass.getEnclosingClass() != null) {
            searchClass = javaClass.getEnclosingClass();
        } else {
            searchClass = javaClass;
        }
        String aliasName = NamingBase.getAliasInstantiatorMethodName(container);
        for (java.lang.reflect.Method method : searchClass.getDeclaredMethods()) {
            if (method.getName().equals(aliasName)) {
                return method;
            }
        }
        return null;
    }
    
    public static java.lang.reflect.Constructor<?> findConstructor(Class<?> javaClass) {
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
            throw Metamodel.newModelError("Couldn't find method " + javaClass);
        }
        if (numBest > 1) {
            throw Metamodel.newModelError("Method arity ambiguity " + javaClass);
        }
        return best;
    }

    public static Annotation[][] getParameterAnnotations(Member methodOrConstructor) {
        Type[] javaParameters;
        Annotation[][] annotations;
        int parameterCount;
        if(methodOrConstructor instanceof Method){
            javaParameters = ((Method)methodOrConstructor).getGenericParameterTypes();
            annotations = ((Method)methodOrConstructor).getParameterAnnotations();
            // only getParameterTypes always reliably include synthetic parameters for constructors
            parameterCount = ((Method)methodOrConstructor).getParameterTypes().length;
        }else{
            javaParameters = ((Constructor<?>)methodOrConstructor).getGenericParameterTypes();
            annotations = ((Constructor<?>)methodOrConstructor).getParameterAnnotations();
            // only getParameterTypes always reliably include synthetic parameters for constructors
            parameterCount = ((Constructor<?>)methodOrConstructor).getParameterTypes().length;
        }
        int start = 0;
        if(methodOrConstructor instanceof Constructor){
            // enums will always add two synthetic parameters (string and int) and always be static so none more
            Class<?> declaringClass = methodOrConstructor.getDeclaringClass();
            if(declaringClass.isEnum())
                start = 2;
            // inner classes will always add a synthetic parameter to the constructor, unless they are static
            // FIXME: local and anonymous classes may add more but we don't know how to find out
            else if((declaringClass.isMemberClass()
                        || declaringClass.isAnonymousClass()
                        // if it's a local class its container method must not be static
                        || (declaringClass.isLocalClass() && !isStaticLocalContainer(declaringClass)))
                    && !Modifier.isStatic(declaringClass.getModifiers()))
                start = 1;
        }
        
        // some compilers will only include non-synthetic parameters in getGenericParameterTypes(), so we need to know if
        // we have less, we should subtract synthetic parameters
        int parametersOffset = javaParameters.length != parameterCount ? -start : 0;
        // if at least one parameter is annotated, java reflection will only include non-synthetic parameters in 
        // getParameterAnnotations(), so we need to know if we have less, we should subtract synthetic parameters
        int annotationsOffset = annotations.length != parameterCount ? -start : 0;
        
        // we have synthetic parameters first (skipped with start), then regular params, then synthetic captured params
        
        // if we have any synthetic params, remove them from the count, except the ones from the start
        // this makes sure we don't consider synthetic captured params
        if(javaParameters.length != parameterCount)
            parameterCount = javaParameters.length + start;
        else if(annotations.length != parameterCount) // better luck with annotations?
            parameterCount = annotations.length + start;

        // skip synthetic parameters
        Annotation[][] ret = new Annotation[parameterCount-start][];
        System.arraycopy(annotations, start+annotationsOffset, ret, 0, parameterCount-start);
        return ret;
    }

    public static boolean isStaticLocalContainer(Class<?> klass) {
        Constructor<?> enclosingConstructor = klass.getEnclosingConstructor();
        if(enclosingConstructor != null)
            return Modifier.isStatic(enclosingConstructor.getModifiers());
        Method enclosingMethod = klass.getEnclosingMethod();
        return Modifier.isStatic(enclosingMethod.getModifiers());
    }
}
