package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.metamodel.Declaration;
import ceylon.language.metamodel.Package$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class Package implements ceylon.language.metamodel.Package, ReifiedType{

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(Package.class);
    
    private com.redhat.ceylon.compiler.typechecker.model.Package declaration;

    private Module module;

    public Package(com.redhat.ceylon.compiler.typechecker.model.Package declaration){
        this.declaration = declaration;
    }
    
    @Override
    @Ignore
    public Package$impl $ceylon$language$metamodel$Package$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return declaration.getNameAsString();
    }

    @Override
    public ceylon.language.metamodel.Module getContainer() {
        // this does not need to be thread-safe as Metamodel.getOrCreateMetamodel is thread-safe so if we
        // assign module twice we get the same result
        if(module == null){
            module = Metamodel.getOrCreateMetamodel(declaration.getModule());
        }
        return module;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Declaration>")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Declaration"))
    public <Kind extends Declaration> Sequential<? extends Declaration> members(@Ignore TypeDescriptor $reifiedKind) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Declaration>")
    @TypeParameters({ 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Declaration"), 
        @TypeParameter(value = "Annotation") 
    })
    public <Kind extends Declaration, Annotation> Sequential<? extends Declaration> annotatedMembers(@Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        // TODO Auto-generated method stub
        return null;
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

}
