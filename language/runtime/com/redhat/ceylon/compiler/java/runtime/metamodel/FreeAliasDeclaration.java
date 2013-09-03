package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.model.Type;
import ceylon.language.model.declaration.AliasDeclaration$impl;
import ceylon.language.model.declaration.OpenType;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public class FreeAliasDeclaration extends FreeTopLevelOrMemberDeclaration 
    implements ceylon.language.model.declaration.AliasDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeAliasDeclaration.class);
    
    public FreeAliasDeclaration(com.redhat.ceylon.compiler.typechecker.model.TypeAlias declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public AliasDeclaration$impl $ceylon$language$model$declaration$AliasDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OpenType getOpenType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Ignore
    @Override
    public Type<? extends Object> apply() {
        return apply(apply$types());
    }

    @Override
    @TypeInfo("ceylon.language.model::Type<ceylon.language::Anything>")
    public Type<? extends Object> apply(@Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Type<ceylon.language::Anything>>") 
        Sequential<? extends Type<? extends Object>> types) {
        
        return bindAndApply(null, types);
    }

    @Override
    @Ignore
    public Sequential<? extends Type<? extends Object>> apply$types() {
        return (Sequential)empty_.$get();
    }

    @Override
    @Ignore
    public Type<? extends Object> bindAndApply(Object instance) {
        return bindAndApply(instance, bindAndApply$types(instance));
    }

    @Override
    @TypeInfo("ceylon.language.model::Type<ceylon.language::Anything>")
    public Type<? extends Object> bindAndApply(@Name("instance") @TypeInfo("ceylon.language::Object") Object instance, 
            @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Type<ceylon.language::Anything>>") 
            Sequential<? extends Type<? extends Object>> types) {
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        // FIXME: this is wrong because it does not include the container type
        com.redhat.ceylon.compiler.typechecker.model.ProducedType appliedType = declaration.getProducedReference(null, producedTypes).getType();
        // FIXME: this is wrong because it does not bind the instance
        return Metamodel.getAppliedMetamodel(appliedType);
    }

    @Override
    @Ignore
    public Sequential<? extends Type<? extends Object>> bindAndApply$types(Object instance) {
        return (Sequential)empty_.$get();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
