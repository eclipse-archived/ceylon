package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import ceylon.language.model.Annotated;
import ceylon.language.model.Annotated$impl;
import ceylon.language.model.declaration.SetterDeclaration;
import ceylon.language.model.declaration.SetterDeclaration$impl;
import ceylon.language.model.declaration.VariableDeclaration;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@Class
@SatisfiedTypes({"ceylon.language.model::Annotated", "ceylon.language.model.declaration::SetterDeclaration"})
public class FreeSetter 
        implements SetterDeclaration, Annotated, AnnotationBearing, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeSetter.class);
    
    private FreeVariable variable;
    
    private Method declaredSetter;

    public FreeSetter(FreeVariable freeVariable) {
        this.variable = freeVariable;
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(variable.declaration);
        String setterName = Naming.getSetterName(variable.declaration);
        this.declaredSetter = Reflection.getDeclaredSetter(javaClass, setterName);
    }

    @Override
    public Annotated$impl $ceylon$language$model$Annotated$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public SetterDeclaration$impl $ceylon$language$model$declaration$SetterDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Annotation[] $getJavaAnnotations() {
        return declaredSetter != null ? declaredSetter.getDeclaredAnnotations() : AnnotationBearing.NONE;
    }
    
    @TypeInfo("ceylon.language.model.declaration::VariableDeclaration")
    @Override
    public VariableDeclaration getVariable() {
        return variable;
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
