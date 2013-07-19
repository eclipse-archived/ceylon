package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;

import ceylon.language.Sequential;
import ceylon.language.metamodel.Annotated$impl;
import ceylon.language.metamodel.declaration.AnnotatedDeclaration$impl;
import ceylon.language.metamodel.declaration.Declaration$impl;
import ceylon.language.metamodel.declaration.TypedDeclaration$impl;
import ceylon.language.metamodel.declaration.OpenType;
import ceylon.language.metamodel.declaration.ParameterDeclaration;
import ceylon.language.metamodel.declaration.ParameterDeclaration$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeParameter implements ParameterDeclaration, ReifiedType, AnnotationBearing {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeParameter.class);
    private com.redhat.ceylon.compiler.typechecker.model.Parameter declaration;
    private OpenType type;
    private Annotation[] annotations;
    
    FreeParameter(com.redhat.ceylon.compiler.typechecker.model.Parameter declaration,
            java.lang.annotation.Annotation[] annotations){
        this.declaration = declaration;
        this.type = Metamodel.getMetamodel(declaration.getType());
        this.annotations = annotations;
    }
    
    @Override
    @Ignore
    public Declaration$impl $ceylon$language$metamodel$declaration$Declaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public TypedDeclaration$impl $ceylon$language$metamodel$declaration$TypedDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AnnotatedDeclaration$impl $ceylon$language$metamodel$declaration$AnnotatedDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public ParameterDeclaration$impl $ceylon$language$metamodel$declaration$ParameterDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @Ignore
    public Annotated$impl $ceylon$language$metamodel$Annotated$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations() {
        return annotations;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Annotation>")
    @TypeParameters(@TypeParameter(value = "Annotation", satisfies = "ceylon.language.metamodel::Annotation<Annotation>"))
    public <Annotation extends ceylon.language.metamodel.Annotation<? extends Annotation>> Sequential<? extends Annotation> annotations(@Ignore TypeDescriptor $reifiedAnnotation) {
        return Metamodel.annotations($reifiedAnnotation, this);
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return declaration.getName();
    }

    @Override
    public boolean getDefaulted(){
        return declaration.isDefaulted();
    }
    
    @Override
    public boolean getVariadic(){
        return declaration.isSequenced();
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::OpenType")
    public OpenType getOpenType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

}
