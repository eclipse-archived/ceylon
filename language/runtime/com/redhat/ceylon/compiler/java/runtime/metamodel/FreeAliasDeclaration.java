package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.model.Type;
import ceylon.language.model.declaration.AliasDeclaration$impl;
import ceylon.language.model.declaration.GenericDeclaration$impl;
import ceylon.language.model.declaration.OpenType;
import ceylon.language.model.declaration.TypeParameter;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public class FreeAliasDeclaration extends FreeTopLevelOrMemberDeclaration 
    implements ceylon.language.model.declaration.AliasDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeAliasDeclaration.class);

    private OpenType openType;

    private boolean initialised = false;

    private Sequential<? extends ceylon.language.model.declaration.TypeParameter> typeParameters;
    
    public FreeAliasDeclaration(com.redhat.ceylon.compiler.typechecker.model.TypeAlias declaration) {
        super(declaration);
        
        this.openType = Metamodel.getMetamodel(declaration.getType().resolveAliases());
    }

    protected final void checkInit(){
        if(!initialised ){
            // FIXME: lock on model loader?
            synchronized(this){
                if(!initialised){
                    init();
                    initialised = true;
                }
            }
        }
    }

    private void init() {
        com.redhat.ceylon.compiler.typechecker.model.TypeAlias declaration = (com.redhat.ceylon.compiler.typechecker.model.TypeAlias) this.declaration;
        
        this.typeParameters = Metamodel.getTypeParameters(declaration);
    }

    @Override
    @Ignore
    public AliasDeclaration$impl $ceylon$language$model$declaration$AliasDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public GenericDeclaration$impl $ceylon$language$model$declaration$GenericDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OpenType getOpenType() {
        return openType;
    }

    @Override
    public TypeParameter getTypeParameterDeclaration(@Name("name") @TypeInfo("ceylon.language::String") String name) {
        return Metamodel.findDeclarationByName(getTypeParameterDeclarations(), name);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.model.declaration::TypeParameter>")
    public Sequential<? extends TypeParameter> getTypeParameterDeclarations() {
        checkInit();
        return typeParameters;
    }

    @Ignore
    @Override
    public Type<? extends Object> apply() {
        return apply((Sequential)empty_.$get());
    }

    @Override
    @TypeInfo("ceylon.language.model::Type<ceylon.language::Anything>")
    public Type<? extends Object> apply(@Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Type<ceylon.language::Anything>>") 
        Sequential<? extends Type<? extends Object>> types) {
        
        return bindAndApply(null, types);
    }

    @Override
    @Ignore
    public Type<? extends Object> bindAndApply(Object instance) {
        return bindAndApply(instance, (Sequential)empty_.$get());
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
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
