package org.eclipse.ceylon.model.loader.impl.reflect.mirror;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.model.loader.mirror.AnnotationMirror;
import org.eclipse.ceylon.model.loader.mirror.TypeMirror;
import org.eclipse.ceylon.model.loader.mirror.VariableMirror;

public class ReflectionVariable implements VariableMirror {

    private Type type;
    private Map<String, AnnotationMirror> annotations;
    private ReflectionType varType;

    public ReflectionVariable(Type type, Annotation[] annotations) {
        this.type = type;
        this.annotations = ReflectionUtils.getAnnotations(annotations);
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        return annotations.get(type);
    }

    @Override
    public Set<String> getAnnotationNames() {
        return annotations.keySet();
    }

    @Override
    public TypeMirror getType() {
        if(varType != null)
            return varType;
        varType = new ReflectionType(type);
        return varType;
    }

    @Override
    public String getName() {
        AnnotationMirror name = getAnnotation("org.eclipse.ceylon.compiler.java.metadata.Name");
        if(name == null)
            return "unknown";
        return (String) name.getValue();
    }

    @Override
    public String toString() {
        return "[ReflectionVariable: "+type.toString()+"]";
    }
}
