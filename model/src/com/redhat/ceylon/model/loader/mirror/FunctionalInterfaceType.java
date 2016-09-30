package com.redhat.ceylon.model.loader.mirror;

import java.util.List;

public class FunctionalInterfaceType {

    private final List<TypeMirror> parameterTypes;
    private final TypeMirror returnType;

    public FunctionalInterfaceType(TypeMirror returnType, List<TypeMirror> parameterTypes) {
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    public List<TypeMirror> getParameterTypes() {
        return parameterTypes;
    }

    public TypeMirror getReturnType() {
        return returnType;
    }
}
