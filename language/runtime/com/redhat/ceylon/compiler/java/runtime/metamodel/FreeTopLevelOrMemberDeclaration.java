package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.metamodel.Annotated$impl;
import ceylon.language.metamodel.declaration.Package;
import ceylon.language.metamodel.declaration.TopLevelOrMemberDeclaration$impl;
import ceylon.language.metamodel.declaration.Declaration$impl;
import ceylon.language.metamodel.declaration.TypedDeclaration$impl;
import ceylon.language.metamodel.declaration.AnnotatedDeclaration$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeTopLevelOrMemberDeclaration 
    implements ceylon.language.metamodel.declaration.TopLevelOrMemberDeclaration, ReifiedType {
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeTopLevelOrMemberDeclaration.class);

    @Ignore
    protected com.redhat.ceylon.compiler.typechecker.model.Declaration declaration;
    
    private Package pkg;

    @Override
    public String toString() {
        String string = declaration.getName();
        Scope container = declaration.getContainer();
        while (container instanceof TypeDeclaration) {
            string = ((TypeDeclaration) container).getName() + '.' + string;
            container = container.getContainer();
        }
        return string;
    }

    public FreeTopLevelOrMemberDeclaration(com.redhat.ceylon.compiler.typechecker.model.Declaration declaration) {
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public TopLevelOrMemberDeclaration$impl $ceylon$language$metamodel$declaration$TopLevelOrMemberDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
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
    public Annotated$impl $ceylon$language$metamodel$Annotated$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        return declaration.getName();
    }

    @Override
    public Package getPackageContainer() {
        // this does not need to be thread-safe as Metamodel.getOrCreateMetamodel is thread-safe so if we
        // assign pkg twice we get the same result
        if(pkg == null){
            pkg = Metamodel.getOrCreateMetamodel(Metamodel.getPackage(declaration));
        }
        return pkg;
    }

    @Override
    public boolean getToplevel() {
        return declaration.isToplevel();
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Annotation>")
    @TypeParameters(@TypeParameter(value = "Annotation", satisfies = "ceylon.language.metamodel::Annotation<Annotation>"))
    public <Annotation extends ceylon.language.metamodel.Annotation<? extends Annotation>> Sequential<? extends Annotation> annotations(@Ignore TypeDescriptor $reifiedAnnotation) {
        return Metamodel.annotations($reifiedAnnotation, this);
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
