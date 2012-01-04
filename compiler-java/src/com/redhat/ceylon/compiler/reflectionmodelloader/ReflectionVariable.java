package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.modelloader.refl.ReflAnnotation;
import com.redhat.ceylon.compiler.modelloader.refl.ReflType;
import com.redhat.ceylon.compiler.modelloader.refl.ReflVariable;

public class ReflectionVariable implements ReflVariable {

    private Type type;
    private Annotation[] annotations;

    public ReflectionVariable(Type type, Annotation[] annotations) {
        this.type = type;
        this.annotations = annotations;
    }

    @Override
    public ReflAnnotation getAnnotation(String type) {
        return ReflectionUtils.getAnnotation(annotations, type);
    }

    @Override
    public ReflType getType() {
        return new ReflectionType(type);
    }

    @Override
    public String getName() {
        ReflAnnotation name = getAnnotation(Name.class.getName());
        if(name == null)
            return "unknown";
        return (String) name.getValue();
    }

}
