package com.redhat.ceylon.model.loader;

import java.util.Arrays;
import java.util.List;

import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.loader.mirror.TypeKind;
import com.redhat.ceylon.model.loader.mirror.TypeMirror;
import com.redhat.ceylon.model.loader.mirror.TypeParameterMirror;

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
