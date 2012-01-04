package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.modelloader.refl.ReflAnnotation;

public class ReflectionAnnotation implements ReflAnnotation {

    private Annotation annotation;

    public ReflectionAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    @Override
    public Object getValue(String fieldName) {
        try {
            Method method = annotation.getClass().getMethod(fieldName);
            return convertValue(method.invoke(annotation));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object convertValue(Object value) {
        if(value.getClass().isArray()){
            Object[] array = (Object[])value;
            List<Object> values = new ArrayList<Object>(array.length);
            for(Object val : array)
                values.add(convertValue(val));
            return values;
        }
        if(value instanceof Annotation){
            return new ReflectionAnnotation((Annotation) value);
        }
        if(value instanceof Enum){
            return ((Enum<?>)value).name();
        }
        return value;
    }

    @Override
    public Object getValue() {
        return getValue("default");
    }

}
