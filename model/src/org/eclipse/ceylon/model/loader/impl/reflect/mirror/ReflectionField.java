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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.model.loader.mirror.AnnotationMirror;
import org.eclipse.ceylon.model.loader.mirror.FieldMirror;
import org.eclipse.ceylon.model.loader.mirror.TypeMirror;

public class ReflectionField implements FieldMirror {

    private Field field;
    private ReflectionType type;
    private Map<String, AnnotationMirror> annotations;

    public ReflectionField(Field field) {
        this.field = field;
    }

    @Override
    public AnnotationMirror getAnnotation(String type) {
        return getAnnotations().get(type);
    }
    
    private Map<String, AnnotationMirror> getAnnotations() {
        // profiling revealed we need to cache this
        if(annotations == null){
            annotations = ReflectionUtils.getAnnotations(field);
        }
        return annotations;
    }

    @Override
    public Set<String> getAnnotationNames() {
        return getAnnotations().keySet();
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(field.getModifiers());
    }
    
    @Override
    public boolean isProtected() {
        return Modifier.isProtected(field.getModifiers());
    }
    
    @Override
    public boolean isDefaultAccess() {
        return !Modifier.isPrivate(field.getModifiers())
            && !Modifier.isPublic(field.getModifiers())
            && !Modifier.isProtected(field.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    @Override
    public TypeMirror getType() {
        if(type != null)
            return type;
        type = new ReflectionType(field.getGenericType());
        return type;
    }

    @Override
    public String toString() {
        return "[ReflectionField: "+field.toString()+"]";
    }
}
