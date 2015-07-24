package com.redhat.ceylon.model.typechecker.model;

import java.util.List;

public class ClassAlias extends Class {
    
    private TypeDeclaration constructor;
    
    public TypeDeclaration getConstructor() {
        return constructor;
    }
    
    public void setConstructor(TypeDeclaration constructor) {
        this.constructor = constructor;
    }
    
    @Override
    public boolean isAlias() {
        return true;
    }
    
    @Override
    public boolean isEmptyType() {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().isEmptyType();
            }
            finally {
                Type.decDepth();
            }
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean isTupleType() {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().isTupleType();
            }
            finally {
                Type.decDepth();
            }
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean isSequentialType() {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().isSequentialType();
            }
            finally {
                Type.decDepth();
            }
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean isSequenceType() {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().isSequenceType();
            }
            finally {
                Type.decDepth();
            }
        }
        else {
            return false;
        }
    }
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        Type et = getExtendedType();
        if (et!=null) { 
            et.getDeclaration()
                .collectSupertypeDeclarations(results);
        }
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().inherits(dec);
            }
            finally {
                Type.decDepth();
            }
        }
        return false;
    }
    
}
