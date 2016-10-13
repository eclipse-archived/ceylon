package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import javax.lang.model.element.AnnotationValueVisitor;

import com.redhat.ceylon.javax.lang.model.element.AnnotationMirror;
import com.redhat.ceylon.javax.lang.model.element.AnnotationValue;
import com.redhat.ceylon.javax.lang.model.element.VariableElement;
import com.redhat.ceylon.javax.lang.model.type.TypeMirror;

public class AnnotationValueFacade implements javax.lang.model.element.AnnotationValue {

    protected AnnotationValue f;

    public AnnotationValueFacade(AnnotationValue f) {
        this.f = f;
    }

    @Override
    public <R, P> R accept(AnnotationValueVisitor<R, P> v, P p) {
        return f.accept(Wrappers.wrap(v), p);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getValue() {
        /* <ul><li> a wrapper class (such as {@link Integer}) for a primitive type
        *     <li> {@code String}
        *     <li> {@code TypeMirror}
        *     <li> {@code VariableElement} (representing an enum constant)
        *     <li> {@code AnnotationMirror}
        *     <li> {@code List<? extends AnnotationValue>}
        *              (representing the elements, in declared order, if the value is an array)
        *   */
        Object value = f.getValue();
        if(value == null)
            return null;
        if(value instanceof String
                || value instanceof Boolean
                || value instanceof Character
                || value instanceof Byte
                || value instanceof Short
                || value instanceof Integer
                || value instanceof Long
                || value instanceof Float
                || value instanceof Double
                )
            return value;
        if(value instanceof TypeMirror)
            return Facades.facade((TypeMirror)value);
        if(value instanceof VariableElement)
            return Facades.facade((VariableElement)value);
        if(value instanceof AnnotationMirror)
            return Facades.facade((AnnotationMirror)value);
        if(value instanceof List)
            return Facades.facadeAnnotationValueList((List<? extends AnnotationValue>) value);
        throw new RuntimeException("Don't know how to facade value type "+value);
    }

}
