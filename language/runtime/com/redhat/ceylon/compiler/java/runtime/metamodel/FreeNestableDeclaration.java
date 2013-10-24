package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.Annotated$impl;
import ceylon.language.meta.declaration.AnnotatedDeclaration$impl;
import ceylon.language.meta.declaration.Declaration$impl;
import ceylon.language.meta.declaration.Module;
import ceylon.language.meta.declaration.Package;
import ceylon.language.meta.declaration.NestableDeclaration$impl;
import ceylon.language.meta.declaration.TypedDeclaration$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeNestableDeclaration 
    implements ceylon.language.meta.declaration.NestableDeclaration, ReifiedType {
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeNestableDeclaration.class);

    @Ignore
    protected com.redhat.ceylon.compiler.typechecker.model.Declaration declaration;
    
    private Package pkg;

    public FreeNestableDeclaration(com.redhat.ceylon.compiler.typechecker.model.Declaration declaration) {
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public NestableDeclaration$impl $ceylon$language$meta$declaration$NestableDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public Declaration$impl $ceylon$language$meta$declaration$Declaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public TypedDeclaration$impl $ceylon$language$meta$declaration$TypedDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public AnnotatedDeclaration$impl $ceylon$language$meta$declaration$AnnotatedDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public Annotated$impl $ceylon$language$Annotated$impl() {
        return null;
    }

    @Override
    public String getName() {
        return declaration.getName();
    }

    @Override
    public String getQualifiedName() {
        return declaration.getQualifiedNameString();
    }

    @Override
    public Package getContainingPackage() {
        // this does not need to be thread-safe as Metamodel.getOrCreateMetamodel is thread-safe so if we
        // assign pkg twice we get the same result
        if(pkg == null){
            pkg = Metamodel.getOrCreateMetamodel(Metamodel.getPackage(declaration));
        }
        return pkg;
    }

    @Override
    public Module getContainingModule() {
        return getContainingPackage().getContainer();
    }

    @Override
    public boolean getToplevel() {
        return declaration.isToplevel();
    }

    @Override
    public boolean getActual() {
        return declaration.isActual();
    }

    @Override
    public boolean getFormal() {
        return declaration.isFormal();
    }

    @Override
    public boolean getDefault() {
        return declaration.isDefault();
    }

    @Override
    public boolean getShared() {
        return declaration.isShared();
    }

    @Override
    @TypeInfo(value = "ceylon.language.meta.declaration::NestableDeclaration|ceylon.language.meta.declaration::Package", erased = true)
    public java.lang.Object getContainer() {
        return Metamodel.getContainer(declaration);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Annotation>")
    @TypeParameters(@TypeParameter(value = "Annotation", satisfies = "ceylon.language::Annotation"))
    public <Annotation extends ceylon.language.Annotation> Sequential<? extends Annotation> annotations(@Ignore TypeDescriptor $reifiedAnnotation) {
        return Metamodel.annotations($reifiedAnnotation, this);
    }

    @Override
    public String toString() {
        return getQualifiedName();
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
