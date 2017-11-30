/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.impl.reflect.mirror;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.model.loader.mirror.TypeMirror;
import org.eclipse.ceylon.model.loader.mirror.TypeParameterMirror;

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
