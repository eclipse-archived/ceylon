package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.model.Annotated$impl;
import ceylon.language.model.declaration.AnnotatedDeclaration$impl;
import ceylon.language.model.declaration.Declaration$impl;
import ceylon.language.model.declaration.Module;
import ceylon.language.model.declaration.Package;
import ceylon.language.model.declaration.TopLevelOrMemberDeclaration$impl;
import ceylon.language.model.declaration.TypedDeclaration$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeTopLevelOrMemberDeclaration 
    implements ceylon.language.model.declaration.TopLevelOrMemberDeclaration, ReifiedType {
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeTopLevelOrMemberDeclaration.class);

    @Ignore
    protected com.redhat.ceylon.compiler.typechecker.model.Declaration declaration;
    
    private Package pkg;

    public FreeTopLevelOrMemberDeclaration(com.redhat.ceylon.compiler.typechecker.model.Declaration declaration) {
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public TopLevelOrMemberDeclaration$impl $ceylon$language$model$declaration$TopLevelOrMemberDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public Declaration$impl $ceylon$language$model$declaration$Declaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public TypedDeclaration$impl $ceylon$language$model$declaration$TypedDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public AnnotatedDeclaration$impl $ceylon$language$model$declaration$AnnotatedDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public Annotated$impl $ceylon$language$model$Annotated$impl() {
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
    public ceylon.language.model.declaration.AnnotatedDeclaration getContainer() {
        return Metamodel.getContainer(declaration);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Annotation>")
    @TypeParameters(@TypeParameter(value = "Annotation", satisfies = "ceylon.language.model::Annotation<Annotation>"))
    public <Annotation extends ceylon.language.model.Annotation<? extends Annotation>> Sequential<? extends Annotation> annotations(@Ignore TypeDescriptor $reifiedAnnotation) {
        return Metamodel.annotations($reifiedAnnotation, this);
    }

    @Override
    public String toString() {
        return getQualifiedName();
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
