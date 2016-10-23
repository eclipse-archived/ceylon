package com.redhat.ceylon.model.loader.mirror;

import java.util.List;

public class FunctionalInterfaceType {

    private final List<TypeMirror> parameterTypes;
    private final TypeMirror returnType;
    private boolean variadic;
    private MethodMirror method;

    public FunctionalInterfaceType(MethodMirror method, TypeMirror returnType, List<TypeMirror> parameterTypes, boolean variadic) {
        this.method = method;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.variadic = variadic;
    }

    public boolean isVariadic() {
        return variadic;
    }
    
    public List<TypeMirror> getParameterTypes() {
        return parameterTypes;
    }

    public TypeMirror getReturnType() {
        return returnType;
    }
    
    public MethodMirror getMethod() {
        return method;
    }
}
