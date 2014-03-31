package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import ceylon.language.Annotated$impl;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.SetterDeclaration;
import ceylon.language.meta.declaration.SetterDeclaration$impl;
import ceylon.language.meta.declaration.ValueDeclaration;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@Class
@SatisfiedTypes({"ceylon.language.meta.declaration::SetterDeclaration"})
public class FreeSetter 
        extends FreeNestableDeclaration
        implements SetterDeclaration, AnnotationBearing {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeSetter.class);
    
    private FreeValue value;
    
    private Method declaredSetter;

    public FreeSetter(com.redhat.ceylon.compiler.typechecker.model.Setter setter) {
        super(setter);
    }

    private void checkInit(){
        // must be deferred because getter/setter refer to one another
        if(value == null){
            synchronized(Metamodel.getLock()){
                if(value == null){
                    com.redhat.ceylon.compiler.typechecker.model.Setter setter = ((com.redhat.ceylon.compiler.typechecker.model.Setter)declaration);
                    this.value = (FreeValue) Metamodel.getOrCreateMetamodel(setter.getGetter());
                    java.lang.Class<?> javaClass = Metamodel.getJavaClass(value.declaration);
                    String setterName = Naming.getSetterName(value.declaration);
                    this.declaredSetter = Reflection.getDeclaredSetter(javaClass, setterName);
                }
            }
        }
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
        checkInit();
        return declaredSetter != null ? declaredSetter.getDeclaredAnnotations() : AnnotationBearing.NONE;
    }
    
    @Override
    public ValueDeclaration getVariable() {
        checkInit();
        return value;
    }

    @Override
    public OpenType getOpenType() {
        checkInit();
        return value.getOpenType();
    }

    @Override
    public int hashCode() {
        return Metamodel.hashCode(this, "setter");
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof FreeSetter == false)
            return false;
        return Metamodel.equalsForSameType(this, (FreeSetter)obj);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
