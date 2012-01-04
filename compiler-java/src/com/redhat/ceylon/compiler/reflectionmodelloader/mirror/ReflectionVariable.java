package com.redhat.ceylon.compiler.reflectionmodelloader.mirror;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.modelloader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.VariableMirror;

public class ReflectionVariable implements VariableMirror {

    private Type type;
    private Annotation[] annotations;

    public ReflectionVariable(Type type, Annotation[] annotations) {
        this.type = type;
        this.annotations = annotations;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        return ReflectionUtils.getAnnotation(annotations, type);
    }

    @Override
    public TypeMirror getType() {
        return new ReflectionType(type);
    }

    @Override
    public String getName() {
        AnnotationMirror name = getAnnotation(Name.class.getName());
        if(name == null)
            return "unknown";
        return (String) name.getValue();
    }

}
