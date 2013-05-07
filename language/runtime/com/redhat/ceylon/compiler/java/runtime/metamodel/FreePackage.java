package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.untyped.Declaration;
import ceylon.language.metamodel.untyped.Package$impl;
import ceylon.language.metamodel.untyped.Value;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreePackage implements ceylon.language.metamodel.untyped.Package, ReifiedType{

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreePackage.class);
    
    private com.redhat.ceylon.compiler.typechecker.model.Package declaration;

    private FreeModule module;

    private Sequential<FreeDeclaration> members;

    public FreePackage(com.redhat.ceylon.compiler.typechecker.model.Package declaration){
        this.declaration = declaration;
    }
    
    @Override
    @Ignore
    public Package$impl $ceylon$language$metamodel$untyped$Package$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return declaration.getNameAsString();
    }

    @Override
    public ceylon.language.metamodel.untyped.Module getContainer() {
        // this does not need to be thread-safe as Metamodel.getOrCreateMetamodel is thread-safe so if we
        // assign module twice we get the same result
        if(module == null){
            module = Metamodel.getOrCreateMetamodel(declaration.getModule());
        }
        return module;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.untyped::Declaration"))
    public <Kind extends ceylon.language.metamodel.untyped.Declaration> Sequential<? extends Kind> members(@Ignore TypeDescriptor $reifiedKind) {
        // this does not need to be thread-safe as Metamodel.getOrCreateMetamodel is thread-safe so if we
        // assign module twice we get the same result
        if(members == null){
            List<com.redhat.ceylon.compiler.typechecker.model.Declaration> modelMembers = declaration.getMembers();
            FreeDeclaration[] members = new FreeDeclaration[modelMembers.size()];
            int i=0;
            for(com.redhat.ceylon.compiler.typechecker.model.Declaration modelDecl : modelMembers){
                members[i++] = Metamodel.getOrCreateMetamodel(modelDecl);
            }
            this.members = Util.sequentialInstance(FreeDeclaration.$TypeDescriptor, members);
        }
        // FIXME
        return null;
//        return members;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.untyped::Value|ceylon.language::Null")
    public ceylon.language.metamodel.untyped.Value getAttribute(String name) {
        com.redhat.ceylon.compiler.typechecker.model.Declaration toplevel = declaration.getMember(name, null, false);
        if(toplevel instanceof com.redhat.ceylon.compiler.typechecker.model.Value == false)
            return null;
        com.redhat.ceylon.compiler.typechecker.model.Value decl = (com.redhat.ceylon.compiler.typechecker.model.Value) toplevel;
        return (FreeValue) Metamodel.getOrCreateMetamodel(decl);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.untyped::Declaration"), 
        @TypeParameter(value = "Annotation") 
    })
    public <Kind extends Declaration, Annotation> Sequential<? extends Kind> annotatedMembers(@Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        // TODO Auto-generated method stub
        return null;
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

}
