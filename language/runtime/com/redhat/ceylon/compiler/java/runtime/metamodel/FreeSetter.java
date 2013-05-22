package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;

import ceylon.language.metamodel.Annotated;
import ceylon.language.metamodel.Annotated$impl;
import ceylon.language.metamodel.untyped.Setter;
import ceylon.language.metamodel.untyped.Setter$impl;
import ceylon.language.metamodel.untyped.Variable;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 5)
@Class
@SatisfiedTypes({"ceylon.language.metamodel::Annotated", "ceylon.language.metamodel.untyped::Setter"})
public class FreeSetter 
        implements Setter, Annotated, AnnotationBearing {

    private FreeVariable variable;

    public FreeSetter(FreeVariable freeVariable) {
        this.variable = freeVariable;
    }

    @Override
    public Annotated$impl $ceylon$language$metamodel$Annotated$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Setter$impl $ceylon$language$metamodel$untyped$Setter$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Annotation[] $getJavaAnnotations() {
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(variable.declaration);
        String setterName = Naming.getSetterName(variable.declaration);
        for (java.lang.reflect.Method method : javaClass.getMethods()) {
            if (method.getName().equals(setterName)
                    && method.getReturnType() == void.class
                    && method.getParameterTypes().length == 1) {
                return method.getDeclaredAnnotations();
            }
        }
        return AnnotationBearing.NONE;
    }
    
    @TypeInfo("ceylon.language.metamodel.untyped::Variable")
    @Override
    public Variable getVariable() {
        return variable;
    }

}
