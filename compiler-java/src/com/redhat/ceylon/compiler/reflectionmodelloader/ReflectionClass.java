package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.modelloader.refl.ReflAnnotation;
import com.redhat.ceylon.compiler.modelloader.refl.ReflClass;
import com.redhat.ceylon.compiler.modelloader.refl.ReflMethod;
import com.redhat.ceylon.compiler.modelloader.refl.ReflPackage;
import com.redhat.ceylon.compiler.modelloader.refl.ReflType;
import com.redhat.ceylon.compiler.modelloader.refl.ReflTypeParameter;

public class ReflectionClass implements ReflClass {

    private Class<?> klass;

    public ReflectionClass(Class<?> klass) {
        this.klass = klass;
    }

    @Override
    public ReflAnnotation getAnnotation(String type) {
        return ReflectionUtils.getAnnotation(klass, type);
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(klass.getModifiers());
    }

    @Override
    public String getQualifiedName() {
        return klass.getName();
    }

    @Override
    public String getSimpleName() {
        return klass.getSimpleName();
    }

    @Override
    public ReflPackage getPackage() {
        return new ReflectionPackage(klass.getPackage());
    }

    @Override
    public boolean isInterface() {
        return klass.isInterface();
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(klass.getModifiers());
    }

    @Override
    public List<ReflMethod> getDirectMethods() {
        Method[] directMethods = klass.getDeclaredMethods();
        Constructor<?>[] directConstructors = klass.getDeclaredConstructors();
        List<ReflMethod> methods = new ArrayList<ReflMethod>(directMethods.length + directConstructors.length);
        for(Method directMethod : directMethods)
            methods.add(new ReflectionMethod(directMethod));
        for(Constructor<?> directConstructor : directConstructors)
            methods.add(new ReflectionMethod(directConstructor));
        return methods;
    }

    @Override
    public ReflType getSuperclass() {
        Type superclass = klass.getGenericSuperclass();
        return superclass != null ? new ReflectionType(superclass) : null;
    }

    @Override
    public List<ReflType> getInterfaces() {
        Type[] javaInterfaces = klass.getGenericInterfaces();
        List<ReflType> interfaces = new ArrayList<ReflType>(javaInterfaces.length);
        for(Type javaInterface : javaInterfaces)
            interfaces.add(new ReflectionType(javaInterface));
        return interfaces;
    }

    @Override
    public List<ReflTypeParameter> getTypeParameters() {
        return ReflectionUtils.getTypeParameters(klass);
    }

    @Override
    public boolean isCeylonToplevelAttribute() {
        return klass.isAnnotationPresent(com.redhat.ceylon.compiler.java.metadata.Attribute.class);
    }

    @Override
    public boolean isCeylonToplevelObject() {
        return klass.isAnnotationPresent(com.redhat.ceylon.compiler.java.metadata.Object.class);
    }

    @Override
    public boolean isCeylonToplevelMethod() {
        return klass.isAnnotationPresent(com.redhat.ceylon.compiler.java.metadata.Method.class);
    }

    @Override
    public boolean isLoadedFromSource() {
        return false;
    }

}
