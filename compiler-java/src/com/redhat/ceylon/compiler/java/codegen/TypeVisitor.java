package com.redhat.ceylon.compiler.java.codegen;

import java.util.List;

import com.redhat.ceylon.model.typechecker.model.Type;

public class TypeVisitor {
    public void visitType(Type t) {
        Type qt = t.getQualifyingType();
        if (qt!=null) {
            visitQualifyingType(t, qt);
        }
        if (t.isUnknown()) {
            visitUnknown();
        }
        else if (t.isUnion()) {
            visitUnion(t.getCaseTypes());
        }
        else if (t.isIntersection()) {
            visitIntersection(t.getSatisfiedTypes());
        }
        else if (t.isNothing()) {
            visitNothing();
        }
        else if (t.isTypeAlias()) {
            visitTypeAlias(t);
        }
        else if (t.isClass()) {
            visitClass(t);
        }
        else if (t.isInterface()) {
            visitInterface(t);
        }
        
    }
    
    public void visitClassOrInterface(Type t) {
        if (!t.getTypeArgumentList().isEmpty()) {
            visitTypeArguments(t, t.getTypeArgumentList());
        }
    }
    
    public void visitClass(Type t) {
        visitClassOrInterface(t);
    }

    public void visitInterface(Type t) {
        visitClassOrInterface(t);
    }

    public void visitTypeAlias(Type t) {
    }

    public void visitTypeArguments(Type typeConstructor, List<Type> typeArguments) {
        for (Type at: typeArguments) {
            visitType(at);
        }
    }

    public void visitNothing() {
    }

    public void visitIntersection(List<Type> intersectionTypes) {
        for (Type st: intersectionTypes) {
            visitType(st);
        }
    }

    public void visitUnion(List<Type> unionTypes) {
        for (Type ct: unionTypes) {
            visitType(ct);
        }
    }

    public void visitUnknown() {
    }

    public void visitQualifyingType(Type qualified, Type qualifying) {
        visitType(qualifying);
    }
    
}
