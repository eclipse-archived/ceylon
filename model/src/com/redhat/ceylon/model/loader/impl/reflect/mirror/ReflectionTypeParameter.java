package com.redhat.ceylon.model.loader.impl.reflect.mirror;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.model.loader.mirror.TypeMirror;
import com.redhat.ceylon.model.loader.mirror.TypeParameterMirror;

public class ReflectionTypeParameter implements TypeParameterMirror {

    private TypeVariable<?> type;
    private ArrayList<TypeMirror> bounds;

    public ReflectionTypeParameter(Type type) {
        this.type = (TypeVariable<?>) type;
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public List<TypeMirror> getBounds() {
        if(bounds != null)
            return bounds;
        Type[] javaBounds = type.getBounds();
        bounds = new ArrayList<TypeMirror>(javaBounds.length);
        for(Type bound : javaBounds)
            bounds.add(new ReflectionType(bound));
        return bounds;
    }

    @Override
    public String toString() {
        return "[ReflectionTypeParameter: "+type.toString()+"]";
    }
}
