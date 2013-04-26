package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.metamodel.Declaration$impl;
import ceylon.language.metamodel.Package;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class Declaration 
    implements ceylon.language.metamodel.Declaration, ReifiedType {
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(Declaration.class);

    @Ignore
    protected com.redhat.ceylon.compiler.typechecker.model.Declaration declaration;

    public Declaration(com.redhat.ceylon.compiler.typechecker.model.Declaration declaration) {
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public Declaration$impl $ceylon$language$metamodel$Declaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        return declaration.getName();
    }

    @Override
    public Package getPackageContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getToplevel() {
        return declaration.isToplevel();
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Annotation>")
    @TypeParameters(@TypeParameter(value = "Annotation", satisfies = "ceylon.language::Object"))
    public <Annotation> Sequential<? extends Annotation> annotations(@Ignore TypeDescriptor $reifiedAnnotation) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
