package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import ceylon.language.Annotated;
import ceylon.language.Annotated$impl;
import ceylon.language.meta.declaration.SetterDeclaration;
import ceylon.language.meta.declaration.SetterDeclaration$impl;
import ceylon.language.meta.declaration.VariableDeclaration;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@Class
@SatisfiedTypes({"ceylon.language.meta.declaration::SetterDeclaration"})
public class FreeSetter 
        implements SetterDeclaration, AnnotationBearing, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeSetter.class);
    
    private FreeVariable variable;
    
    private Method declaredSetter;

    public FreeSetter(FreeVariable freeVariable) {
        this.variable = freeVariable;
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(variable.declaration);
        String setterName = Naming.getSetterName(variable.declaration);
        this.declaredSetter = Reflection.getDeclaredSetter(javaClass, setterName);
    }

    @Override
    public Annotated$impl $ceylon$language$Annotated$impl() {
        return null;
    }
    
    @Override
    public SetterDeclaration$impl $ceylon$language$meta$declaration$SetterDeclaration$impl() {
        return null;
    }

    @Override
    public Annotation[] $getJavaAnnotations$() {
        return declaredSetter != null ? declaredSetter.getDeclaredAnnotations() : AnnotationBearing.NONE;
    }
    
    @TypeInfo("ceylon.language.meta.declaration::VariableDeclaration")
    @Override
    public VariableDeclaration getVariable() {
        return variable;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getVariable().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.declaration.SetterDeclaration == false)
            return false;
        ceylon.language.meta.declaration.SetterDeclaration other = (ceylon.language.meta.declaration.SetterDeclaration) obj;
        return getVariable().equals(other.getVariable());
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
