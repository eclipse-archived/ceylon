package com.redhat.ceylon.compiler.reflectionmodelloader.mirror;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.modelloader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.PackageMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.TypeParameterMirror;

public class ReflectionClass implements ClassMirror {

    private Class<?> klass;

    public ReflectionClass(Class<?> klass) {
        this.klass = klass;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
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
    public PackageMirror getPackage() {
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
    public List<MethodMirror> getDirectMethods() {
        Method[] directMethods = klass.getDeclaredMethods();
        Constructor<?>[] directConstructors = klass.getDeclaredConstructors();
        List<MethodMirror> methods = new ArrayList<MethodMirror>(directMethods.length + directConstructors.length);
        for(Method directMethod : directMethods)
            methods.add(new ReflectionMethod(directMethod));
        for(Constructor<?> directConstructor : directConstructors)
            methods.add(new ReflectionMethod(directConstructor));
        return methods;
    }

    @Override
    public TypeMirror getSuperclass() {
        Type superclass = klass.getGenericSuperclass();
        return superclass != null ? new ReflectionType(superclass) : null;
    }

    @Override
    public List<TypeMirror> getInterfaces() {
        Type[] javaInterfaces = klass.getGenericInterfaces();
        List<TypeMirror> interfaces = new ArrayList<TypeMirror>(javaInterfaces.length);
        for(Type javaInterface : javaInterfaces)
            interfaces.add(new ReflectionType(javaInterface));
        return interfaces;
    }

    @Override
    public List<TypeParameterMirror> getTypeParameters() {
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
