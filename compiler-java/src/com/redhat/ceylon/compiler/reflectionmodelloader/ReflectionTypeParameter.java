package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.modelloader.refl.ReflType;
import com.redhat.ceylon.compiler.modelloader.refl.ReflTypeParameter;

public class ReflectionTypeParameter implements ReflTypeParameter {

    private TypeVariable<?> type;

    public ReflectionTypeParameter(Type type) {
        this.type = (TypeVariable<?>) type;
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public List<ReflType> getBounds() {
        Type[] javaBounds = type.getBounds();
        List<ReflType> bounds = new ArrayList<ReflType>(javaBounds.length);
        for(Type bound : javaBounds)
            bounds.add(new ReflectionType(bound));
        return bounds;
    }

}
