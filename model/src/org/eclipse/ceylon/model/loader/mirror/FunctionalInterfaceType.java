/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.mirror;

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
