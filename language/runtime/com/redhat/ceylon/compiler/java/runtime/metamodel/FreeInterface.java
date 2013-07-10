package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.metamodel.declaration.InterfaceDeclaration$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeInterface 
    extends FreeClassOrInterface
    implements ceylon.language.metamodel.declaration.InterfaceDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeInterface.class);
    
    public FreeInterface(com.redhat.ceylon.compiler.typechecker.model.Interface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public InterfaceDeclaration$impl $ceylon$language$metamodel$declaration$InterfaceDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.Type> apply$types(){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public ceylon.language.metamodel.Interface<? extends Object> apply(){
        return apply(apply$types());
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Interface<ceylon.language::Anything>")
    public ceylon.language.metamodel.Interface<? extends Object> apply(
            @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") 
            Sequential<? extends ceylon.language.metamodel.Type> types){
        return bindAndApply(null, types);
    }
    
    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.Type> bindAndApply$types(Object instance){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public ceylon.language.metamodel.Interface<? extends Object> bindAndApply(Object instance){
        return bindAndApply(instance, bindAndApply$types(instance));
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Interface<ceylon.language::Anything>")
    public ceylon.language.metamodel.Interface<? extends Object> bindAndApply(
            @Name("instance") @TypeInfo("ceylon.language::Object") Object instance,
            @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") 
            Sequential<? extends ceylon.language.metamodel.Type> types){
        // FIXME: refactor with FreeClass.bindAndApply
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        // FIXME: this is wrong because it does not include the container type
        com.redhat.ceylon.compiler.typechecker.model.ProducedType appliedInterfaceType = declaration.getProducedReference(null, producedTypes).getType();
        return (AppliedInterfaceType)Metamodel.getAppliedMetamodel(appliedInterfaceType);
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
