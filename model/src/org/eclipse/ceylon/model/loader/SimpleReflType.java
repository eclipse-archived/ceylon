/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ceylon.model.loader.mirror.ClassMirror;
import org.eclipse.ceylon.model.loader.mirror.TypeKind;
import org.eclipse.ceylon.model.loader.mirror.TypeMirror;
import org.eclipse.ceylon.model.loader.mirror.TypeParameterMirror;

/**
 * Simple Type Mirror.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class SimpleReflType implements TypeMirror {
 
    public enum Module {
        CEYLON, JDK;
    }

    private String name;
    private TypeKind kind;
    private TypeMirror[] typeParameters;
    private Module module;

    public SimpleReflType(String name, Module module, TypeKind kind, TypeMirror... typeParameters) {
        this.name = name;
        this.kind = kind;
        this.typeParameters = typeParameters;
        this.module = module;
    }
    
    public String toString() {
        String p = Arrays.toString(typeParameters);
        return getClass().getSimpleName() + " of " + name + "<" + p.substring(1, p.length()-1) + ">";
    }

    @Override
    public String getQualifiedName() {
        return name;
    }

    @Override
    public List<TypeMirror> getTypeArguments() {
        return Arrays.asList(typeParameters);
    }

    @Override
    public TypeKind getKind() {
        return kind;
    }

    @Override
    public TypeMirror getComponentType() {
        return null;
    }

    @Override
    public boolean isPrimitive() {
        return kind.isPrimitive();
    }

    @Override
    public TypeMirror getUpperBound() {
        return null;
    }

    @Override
    public TypeMirror getLowerBound() {
        return null;
    }

    @Override
    public boolean isRaw() {
        return false;
    }

    @Override
    public ClassMirror getDeclaredClass() {
        return null;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public TypeParameterMirror getTypeParameter() {
        return null;
    }

    @Override
    public TypeMirror getQualifyingType() {
        return null;
    }
}
