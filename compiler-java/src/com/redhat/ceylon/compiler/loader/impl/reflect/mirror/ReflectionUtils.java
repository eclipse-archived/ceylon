/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.loader.impl.reflect.mirror;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeParameterMirror;

public class ReflectionUtils {

    public static AnnotationMirror getAnnotation(AnnotatedElement annotated, String type) {
        return getAnnotation(annotated.getDeclaredAnnotations(), type);
    }

    public static AnnotationMirror getAnnotation(Annotation[] annotations, String type) {
        for(Annotation annotation : annotations){
            if(annotation.annotationType().getName().equals(type))
                return new ReflectionAnnotation(annotation);
        }
        return null;
    }

    public static List<TypeParameterMirror> getTypeParameters(GenericDeclaration decl) {
        TypeVariable<?>[] javaTypeParameters = decl.getTypeParameters();
        List<TypeParameterMirror> typeParameters = new ArrayList<TypeParameterMirror>(javaTypeParameters.length);
        for(Type javaTypeParameter : javaTypeParameters)
            typeParameters.add(new ReflectionTypeParameter(javaTypeParameter));
        return typeParameters;
    }

    /* Required to fix the distribution build, so that we can build the compiler before building the language module */
    @SuppressWarnings("unchecked")
    static Class<? extends Annotation> getClass(String string) {
        try {
            return (Class<? extends Annotation>) Class.forName(string);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPackageName(Class<?> klass) {
        if(klass.isPrimitive() || klass.isArray()){
            // primitives and arrays don't have a package, so we pretend they come from java.lang
            return "java.lang";
        }else if(klass.getPackage() != null){
            // short road
            return klass.getPackage().getName();
        }else{
            // long road
            while(klass.getEnclosingClass() != null){
                klass = klass.getEnclosingClass();
            }
            String name = klass.getName();
            int lastDot = name.lastIndexOf('.');
            if(lastDot == -1)
                return "";//empty package
            else
                return name.substring(0, lastDot);
        }
    }

    private enum OverXing {
        Overloading, Overriding
    }

    public static boolean isOverridingMethod(Method method) {
        return isOverXingMethod(OverXing.Overriding, method);
    }

    public static boolean isOverloadingMethod(Method method) {
        return isOverXingMethod(OverXing.Overloading, method);
    }

    private static boolean isOverXingMethod(OverXing searchType, Method method) {
        // fast exit
        if(Modifier.isPrivate(method.getModifiers()))
            return false;
        String name = method.getName();
        Class<?>[] parameterTypes;
        parameterTypes = ((Method)method).getParameterTypes();
        Class<?> declaringClass = method.getDeclaringClass();
        // make sure we don't visit interfaces more than once
        Set<Class<?>> visited = new HashSet<Class<?>>();
        // try the superclass first
        Class<?> superclass = declaringClass.getSuperclass();
        if(superclass != null){
            if(isOverXingMethodInClassRecursive(searchType, method, name, parameterTypes, declaringClass, superclass, visited))
                return true;
        }
        // now try the interfaces
        for(Class<?> interfce : declaringClass.getInterfaces()){
            if(isOverXingMethodInClassRecursive(searchType, method, name, parameterTypes, declaringClass, interfce, visited))
                return true;
        }
        // not overriding anything
        return false;
    }
    
    private static boolean isOverXingMethodInClassRecursive(OverXing searchType, Method method, String name, Class<?>[] parameterTypes, Class<?> declaringClass, Class<?> klass,
            Set<Class<?>> visited) {
        if(!visited.add(klass))
            return false;
        if(isOverXingMethodInClass(searchType, method, name, parameterTypes, declaringClass, klass))
            return true;
        // try the superclass first
        Class<?> superclass = klass.getSuperclass();
        if(superclass != null){
            if(isOverXingMethodInClassRecursive(searchType, method, name, parameterTypes, declaringClass, superclass, visited))
                return true;
        }
        // now try the interfaces
        for(Class<?> interfce : klass.getInterfaces()){
            if(isOverXingMethodInClassRecursive(searchType, method, name, parameterTypes, declaringClass, interfce, visited))
                return true;
        }
        // not overriding anything here
        return false;
    }

    private static boolean isOverXingMethodInClass(OverXing searchType, Method method, String name, Class<?>[] parameterTypes, Class<?> declaringClass, Class<?> klass) {
        switch(searchType){
        case Overloading:
            return isOverloadingMethodInClass(method, name, parameterTypes, declaringClass, klass);
        case Overriding:
            return isOverridingMethodInClass(method, name, parameterTypes, declaringClass, klass);
        default:
            throw new RuntimeException("Non-exhaustive switch");
        }
    }

    private static boolean isOverridingMethodInClass(Method method, String name, Class<?>[] parameterTypes, Class<?> declaringClass, Class<?> lookupClass) {
        Class<? extends Annotation> ignoreAnnotationClass = ReflectionUtils.getClass(AbstractModelLoader.CEYLON_IGNORE_ANNOTATION);
        try {
            Method m = lookupClass.getDeclaredMethod(name, parameterTypes);
            // present
            return !m.isBridge() && !m.isSynthetic() && !Modifier.isPrivate(m.getModifiers())
                    && !m.isAnnotationPresent(ignoreAnnotationClass);
        } catch (Exception e) {
            // not present
        }
        NEXT_METHOD:
        for(Method m : lookupClass.getDeclaredMethods()){
            if(!m.getName().equals(name)
                    || m.isBridge()
                    || m.isSynthetic()
                    || m.isAnnotationPresent(ignoreAnnotationClass)
                    || Modifier.isFinal(m.getModifiers())
                    || Modifier.isPrivate(m.getModifiers()))
                continue;
            if(m.getParameterTypes().length != parameterTypes.length)
                continue;
            int i=0;
            // get the type argument mappings for that method's container
            // FIXME: do we need to cache this?
            Map<TypeVariable<?>, Class<?>> typeArguments = getTypeArguments(declaringClass, m.getDeclaringClass(), Collections.<TypeVariable<?>, Class<?>>emptyMap());
            for(Type t : m.getGenericParameterTypes()){
                Class<?> parameterErasure = getParameterErasure(typeArguments, t);
                if(parameterErasure != parameterTypes[i++])
                    continue NEXT_METHOD;
            }
            // must be the same?
            return true;
        }
        return false;
    }

    private static boolean isOverloadingMethodInClass(Method method, String name, Class<?>[] parameterTypes, Class<?> declaringClass, Class<?> lookupClass) {
        Class<? extends Annotation> ignoreAnnotationClass = ReflectionUtils.getClass(AbstractModelLoader.CEYLON_IGNORE_ANNOTATION);
        for(Method m : lookupClass.getDeclaredMethods()){
            if(!m.getName().equals(name)
                    || m.isBridge()
                    || m.isSynthetic()
                    || m.isAnnotationPresent(ignoreAnnotationClass)
                    || Modifier.isPrivate(m.getModifiers()))
                continue;
            if(m.getParameterTypes().length != parameterTypes.length)
                return true;
            int i=0;
            // get the type argument mappings for that method's container
            // FIXME: do we need to cache this?
            Map<TypeVariable<?>, Class<?>> typeArguments = getTypeArguments(declaringClass, m.getDeclaringClass(), Collections.<TypeVariable<?>, Class<?>>emptyMap());
            for(Type t : m.getGenericParameterTypes()){
                Class<?> parameterErasure = getParameterErasure(typeArguments, t);
                if(parameterErasure != parameterTypes[i++])
                    return true;
            }
            // must be the overriding, check the next one
        }
        // no overload here
        return false;
    }

    private static Class<?> getParameterErasure(Map<TypeVariable<?>, Class<?>> typeArguments, Type t) {
        if(t instanceof Class){
            // non-parameterised, must be the same as the one we're looking up
            return (Class<?>) t;
        }else if(t instanceof TypeVariable){
            Class<?> parameterErasure = typeArguments.get(t);
            /*
             * Erasure bounds Note:
             * In practice it looks like we never need erasure bounds, because even if we implement a raw interface,
             * the overriding method will have its parameter types erased to the proper classes (apparently it can't
             * substitute new type variables for the parameters, if it implements a raw interface and overrides a
             * method whose arguments are generics, so it must be classes) and when we look up the method with the
             * parameter types we will find them directly with this method's parameter types in the direct lookup above.
             */
            if(parameterErasure == null)
                parameterErasure = Object.class;
            return parameterErasure;
        }else if(t instanceof ParameterizedType){
            return (Class<?>) ((ParameterizedType) t).getRawType();
        }else if(t instanceof GenericArrayType){
            GenericArrayType gt = (GenericArrayType) t;
            Class<?> componentType = getParameterErasure(typeArguments, gt.getGenericComponentType());
            try {
                ClassLoader classLoader = componentType.getClassLoader();
                String name;
                if(componentType.isArray()){
                    // just add a leading "["
                    name = "["+componentType.getName();
                }else if(componentType == boolean.class){
                    name = "[Z";
                }else if(componentType == byte.class){
                    name = "[B";
                }else if(componentType == char.class){
                    name = "[C";
                }else if(componentType == double.class){
                    name = "[D";
                }else if(componentType == float.class){
                    name = "[F";
                }else if(componentType == int.class){
                    name = "[I";
                }else if(componentType == long.class){
                    name = "[J";
                }else if(componentType == short.class){
                    name = "[S";
                }else{
                    // must be an object non-array class
                    name = "[L"+componentType.getName()+";";
                }
                return classLoader != null ? classLoader.loadClass(name) : Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not find generic array type: "+t, e);
            }
        }else
            throw new RuntimeException("Unknown parameter type: "+t);
    }

    private static Map<TypeVariable<?>, Class<?>> getTypeArguments(Class<?> base, Class<?> searchedSuperType, Map<TypeVariable<?>, Class<?>> baseTypeArguments){
        // fast exit for non-generics
        if(searchedSuperType.getTypeParameters().length == 0)
            return Collections.<TypeVariable<?>, Class<?>>emptyMap();
        
        if(base == searchedSuperType){
            return baseTypeArguments;
        }
        Map<TypeVariable<?>, Class<?>> ret = null;
        // look for it in our super class
        if(base.getSuperclass() != null){
            Type superclass = base.getGenericSuperclass();
            ret = getTypeArgumentsForSuperType(superclass, searchedSuperType, baseTypeArguments);
        }
        if(ret != null)
            return ret;
        // if not, look for interfaces, but only if the super type in question is an interface, otherwise there's no point
        if(searchedSuperType.isInterface()){
            for(Type superinterface : base.getGenericInterfaces()){
                ret = getTypeArgumentsForSuperType(superinterface, searchedSuperType, baseTypeArguments);
                if(ret != null)
                    return ret;
            }
        }
        // no match
        return null;
    }

    private static Map<TypeVariable<?>, Class<?>> getTypeArgumentsForSuperType(Type superclass, Class<?> searchedSuperType, Map<TypeVariable<?>, Class<?>> baseTypeArguments) {
        if(superclass instanceof Class){
            // not generic, or raw
            Class<?> sc = (Class<?>) superclass;
            return getTypeArguments(sc, searchedSuperType, getTypeArguments(sc, Collections.<TypeVariable<?>, Class<?>>emptyMap()));
        }else if(superclass instanceof ParameterizedType){
            Map<TypeVariable<?>, Class<?>> newTypeArgs = getTypeArgumentsMap((ParameterizedType) superclass, baseTypeArguments);
            Class<?> sc = (Class<?>) ((ParameterizedType) superclass).getRawType();
            return getTypeArguments(sc, searchedSuperType, newTypeArgs);
        }else{
            throw new RuntimeException("Unknown superclass type: "+superclass);
        }
    }

    private static Map<TypeVariable<?>, Class<?>> getTypeArgumentsMap(ParameterizedType pt, Map<TypeVariable<?>, Class<?>> baseTypeArguments) {
        Map<TypeVariable<?>, Class<?>> typeArgsMap = new HashMap<TypeVariable<?>, Class<?>>();
        addTypeArguments(pt, typeArgsMap, baseTypeArguments);
        while(pt.getOwnerType() instanceof ParameterizedType){
            pt = (ParameterizedType) pt.getOwnerType();
            addTypeArguments(pt, typeArgsMap, baseTypeArguments);
        }
        return typeArgsMap;
    }

    private static void addTypeArguments(ParameterizedType pt, Map<TypeVariable<?>, Class<?>> typeArgsMap, Map<TypeVariable<?>, Class<?>> baseTypeArguments) {
        Class<?> sc = (Class<?>) pt.getRawType();
        Type[] typeArguments = pt.getActualTypeArguments();
        int i=0;
        for(TypeVariable<?> tv : sc.getTypeParameters()){
            Type ta = typeArguments[i++];
            Class<?> erasedTa = getParameterErasure(baseTypeArguments, ta);
            typeArgsMap.put(tv, erasedTa);
        }
    }

    private static Map<TypeVariable<?>, Class<?>> getTypeArguments(Class<?> base, Map<TypeVariable<?>, Class<?>> baseTypeArguments) {
        Map<TypeVariable<?>, Class<?>> ret = new HashMap<TypeVariable<?>, Class<?>>();
        for(TypeVariable<?> tv : base.getTypeParameters()){
            Class<?> typeArg = baseTypeArguments.get(tv);
            /*
             * See Erasure bounds Note above
             */
            if(typeArg == null)
                typeArg = Object.class;
            ret.put(tv, typeArg);
        }
        return ret;
    }

}
