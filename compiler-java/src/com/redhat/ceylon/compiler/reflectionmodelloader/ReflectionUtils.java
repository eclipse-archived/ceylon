package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.modelloader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.TypeParameterMirror;

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


}
