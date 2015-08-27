package com.redhat.ceylon.model.loader.impl.reflect.mirror;

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

import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.model.loader.mirror.TypeParameterMirror;

public class ReflectionUtils {

    static final Class<? extends Annotation> IGNORE_ANNOTATION = ReflectionUtils.getClass(AbstractModelLoader.CEYLON_IGNORE_ANNOTATION);
    
    public static Map<String, AnnotationMirror> getAnnotations(AnnotatedElement annotated) {
        Annotation[] annotations = annotated.getDeclaredAnnotations();
        return getAnnotations(annotations);
    }

    public static Map<String, AnnotationMirror> getAnnotations(Annotation[] annotations) {
        if(annotations.length == 0)
            return Collections.<String,AnnotationMirror>emptyMap();
        Map<String, AnnotationMirror> map = new HashMap<String,AnnotationMirror>();
        for(int i=annotations.length-1;i>=0;i--){
            Annotation annotation = annotations[i];
            map.put(annotation.annotationType().getName(), new ReflectionAnnotation(annotation));
        }
        return map;
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
    private static Class<? extends Annotation> getClass(String string) {
        try {
            return (Class<? extends Annotation>) Class.forName(string);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPackageName(Class<?> klass) {
//        Package pkg;
        if(klass.isPrimitive() || klass.isArray()){
            // primitives and arrays don't have a package, so we pretend they come from java.lang
            return "java.lang";
            
/*
 * We used to have the following code, but getPackage() is stateless and does a bunch of expensive things
 * every time we invoke it, and even invoking it once, it allocates more crap than the long road below,
 * as proved by profiling, so we use the long road which is cheaper.
        }else if((pkg = klass.getPackage()) != null){
            // short road
            return pkg.getName();
*/
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
        try {
            Method m = lookupClass.getDeclaredMethod(name, parameterTypes);
            // present
            return !m.isBridge() && !m.isSynthetic() && !Modifier.isPrivate(m.getModifiers())
                    && !m.isAnnotationPresent(IGNORE_ANNOTATION)
                    && !isHiddenMethod(m);
        } catch (Exception e) {
            // not present
        }
        NEXT_METHOD:
        for(Method m : lookupClass.getDeclaredMethods()){
            if(!m.getName().equals(name)
                    || m.isBridge()
                    || m.isSynthetic()
                    || m.isAnnotationPresent(IGNORE_ANNOTATION)
                    || Modifier.isFinal(m.getModifiers())
                    || Modifier.isPrivate(m.getModifiers())
                    || isHiddenMethod(m))
                continue;
            if(m.getParameterTypes().length != parameterTypes.length)
                continue;
            int i=0;
            try {
                // get the type argument mappings for that method's container
                // FIXME: do we need to cache this?
                Map<TypeVariable<?>, Class<?>> typeArguments = getTypeArguments(declaringClass, m.getDeclaringClass(), Collections.<TypeVariable<?>, Class<?>>emptyMap());
                for(Type t : m.getGenericParameterTypes()){
                    try{
                        Class<?> parameterErasure = getParameterErasure(typeArguments, t);
                        if(parameterErasure != parameterTypes[i++])
                            continue NEXT_METHOD;
                    }catch(ClassNotFoundException x){
                        // if we can't get the param types, we're not overloading/overriding it anyways
                        continue NEXT_METHOD;
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Can't find type arguments for "+declaringClass, e);
            }
            // must be the same?
            return true;
        }
        return false;
    }

    private static boolean isOverloadingMethodInClass(Method method, String name, Class<?>[] parameterTypes, Class<?> declaringClass, Class<?> lookupClass) {
        for(Method m : lookupClass.getDeclaredMethods()){
            if(!m.getName().equals(name)
                    || m.isBridge()
                    || m.isSynthetic()
                    || m.isAnnotationPresent(IGNORE_ANNOTATION)
                    || Modifier.isPrivate(m.getModifiers())
                    || isHiddenMethod(m))
                continue;
            if(m.getParameterTypes().length != parameterTypes.length)
                return true;
            int i=0;
            try{
                // get the type argument mappings for that method's container
                // FIXME: do we need to cache this?
                Map<TypeVariable<?>, Class<?>> typeArguments = getTypeArguments(declaringClass, m.getDeclaringClass(), Collections.<TypeVariable<?>, Class<?>>emptyMap());
                for(Type t : m.getGenericParameterTypes()){
                    try{
                        Class<?> parameterErasure = getParameterErasure(typeArguments, t);
                        if(parameterErasure != parameterTypes[i++])
                            return true;
                    }catch(ClassNotFoundException x){
                        // if we can't get the param types, we're overloading it anyways
                        return true;
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Can't find type arguments for "+declaringClass, e);
            }
            // must be the overriding, check the next one
        }
        // no overload here
        return false;
    }

    private static boolean isHiddenMethod(Method m) {
        return m.getDeclaringClass() == Object.class
                && (m.getName().equals("finalize")
                        || m.getName().equals("clone"));
    }

    private static Class<?> getParameterErasure(Map<TypeVariable<?>, Class<?>> typeArguments, Type t) throws ClassNotFoundException {
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
        }else
            throw new RuntimeException("Unknown parameter type: "+t);
    }

    private static Map<TypeVariable<?>, Class<?>> getTypeArguments(Class<?> base, Class<?> searchedSuperType, Map<TypeVariable<?>, Class<?>> baseTypeArguments) throws ClassNotFoundException{
        // fast exit for non-generics
        if(searchedSuperType.getTypeParameters().length == 0)
            return Collections.<TypeVariable<?>, Class<?>>emptyMap();
        
        if(base.equals(searchedSuperType)){
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

    private static Map<TypeVariable<?>, Class<?>> getTypeArgumentsForSuperType(Type superclass, Class<?> searchedSuperType, Map<TypeVariable<?>, Class<?>> baseTypeArguments) throws ClassNotFoundException {
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

    private static Map<TypeVariable<?>, Class<?>> getTypeArgumentsMap(ParameterizedType pt, Map<TypeVariable<?>, Class<?>> baseTypeArguments) throws ClassNotFoundException {
        Map<TypeVariable<?>, Class<?>> typeArgsMap = new HashMap<TypeVariable<?>, Class<?>>();
        addTypeArguments(pt, typeArgsMap, baseTypeArguments);
        while(pt.getOwnerType() instanceof ParameterizedType){
            pt = (ParameterizedType) pt.getOwnerType();
            addTypeArguments(pt, typeArgsMap, baseTypeArguments);
        }
        return typeArgsMap;
    }

    private static void addTypeArguments(ParameterizedType pt, Map<TypeVariable<?>, Class<?>> typeArgsMap, Map<TypeVariable<?>, Class<?>> baseTypeArguments) throws ClassNotFoundException {
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
