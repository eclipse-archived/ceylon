package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.Module$impl;
import ceylon.language.metamodel.Package;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public class Module implements ceylon.language.metamodel.Module, ReifiedType{

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(Module.class);
    private com.redhat.ceylon.compiler.typechecker.model.Module declaration;
    private Sequential<Package> packages;
    
    public Module(com.redhat.ceylon.compiler.typechecker.model.Module declaration) {
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public Module$impl $ceylon$language$metamodel$Module$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Package>")
    public Sequential<? extends Package> getMembers() {
        // no need to synchronise as concurrent invocations should get the same array back
        if(this.packages == null){
            List<com.redhat.ceylon.compiler.typechecker.model.Package> modelPackages = declaration.getAllPackages();
            Package[] packages = new Package[modelPackages.size()];
            for(int i=0;i<packages.length;i++){
                packages[i] = Metamodel.getOrCreateMetamodel(modelPackages.get(i));
            }
            this.packages = Util.sequentialInstance(Package.$TypeDescriptor, packages);
        }
        return this.packages;
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getName() {
        return declaration.getNameAsString();
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String getVersion() {
        return declaration.getVersion();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
