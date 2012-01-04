package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.modelloader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.modelloader.mirror.TypeParameterMirror;

public class ReflectionTypeParameter implements TypeParameterMirror {

    private TypeVariable<?> type;

    public ReflectionTypeParameter(Type type) {
        this.type = (TypeVariable<?>) type;
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public List<TypeMirror> getBounds() {
        Type[] javaBounds = type.getBounds();
        List<TypeMirror> bounds = new ArrayList<TypeMirror>(javaBounds.length);
        for(Type bound : javaBounds)
            bounds.add(new ReflectionType(bound));
        return bounds;
    }

}
