package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.modelloader.refl.ReflAnnotation;
import com.redhat.ceylon.compiler.modelloader.refl.ReflMethod;
import com.redhat.ceylon.compiler.modelloader.refl.ReflType;
import com.redhat.ceylon.compiler.modelloader.refl.ReflTypeParameter;
import com.redhat.ceylon.compiler.modelloader.refl.ReflVariable;

public class ReflectionMethod implements ReflMethod {

    private Member method;

    public ReflectionMethod(Member method) {
        this.method = method;
    }

    @Override
    public ReflAnnotation getAnnotation(String type) {
        return ReflectionUtils.getAnnotation((AnnotatedElement)method, type);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(method.getModifiers());
    }

    @Override
    public boolean isConstructor() {
        return method instanceof Constructor;
    }

    @Override
    public boolean isStaticInit() {
        return false;
    }

    @Override
    public List<ReflVariable> getParameters() {
        Type[] javaParameters;
        Annotation[][] annotations;
        if(method instanceof Method){
            javaParameters = ((Method)method).getGenericParameterTypes();
            annotations = ((Method)method).getParameterAnnotations();
        }else{
            javaParameters = ((Constructor<?>)method).getGenericParameterTypes();
            annotations = ((Constructor<?>)method).getParameterAnnotations();
        }
        List<ReflVariable> parameters = new ArrayList<ReflVariable>(javaParameters.length);
        for(int i=0;i<javaParameters.length;i++)
            parameters.add(new ReflectionVariable(javaParameters[i], annotations[i]));
        return parameters;
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(method.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(method.getModifiers());
    }

    @Override
    public ReflType getReturnType() {
        return new ReflectionType(((Method)method).getGenericReturnType());
    }

    @Override
    public List<ReflTypeParameter> getTypeParameters() {
        return ReflectionUtils.getTypeParameters((GenericDeclaration) method);
    }

    public boolean isOverridingMethod() {
        String name = method.getName();
        Class<?>[] parameterTypes;
        if(method instanceof Method)
            parameterTypes = ((Method)method).getParameterTypes();
        else
            parameterTypes = ((Constructor<?>)method).getParameterTypes();
        Class<?> declaringClass = method.getDeclaringClass();
        // try the superclass first
        Class<?> superclass = declaringClass.getSuperclass();
        if(superclass != null){
            try {
                superclass.getMethod(name, parameterTypes);
                // present
                return true;
            } catch (Exception e) {
                // not present
            }
        }
        // now try the interfaces
        for(Class<?> interfce : declaringClass.getInterfaces()){
            try {
                interfce.getMethod(name, parameterTypes);
                // present
                return true;
            } catch (Exception e) {
                // not present
            }
        }
        // not overriding anything
        return false;
    }

}
