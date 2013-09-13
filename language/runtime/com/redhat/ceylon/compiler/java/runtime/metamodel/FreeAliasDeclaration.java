package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.model.Type;
import ceylon.language.model.declaration.AliasDeclaration$impl;
import ceylon.language.model.declaration.AnnotatedDeclaration;
import ceylon.language.model.declaration.GenericDeclaration$impl;
import ceylon.language.model.declaration.OpenType;
import ceylon.language.model.declaration.TypeParameter;

import com.redhat.ceylon.compiler.java.Util;
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
            synchronized(Metamodel.getLock()){
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
        return null;
    }

    @Override
    @Ignore
    public GenericDeclaration$impl $ceylon$language$model$declaration$GenericDeclaration$impl() {
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
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
    public int hashCode() {
        int result = 1;
        java.lang.Object container = getContainer();
        result = 37 * result + (container == null ? 0 : container.hashCode());
        result = 37 * result + getName().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.model.declaration.AliasDeclaration == false)
            return false;
        ceylon.language.model.declaration.AliasDeclaration other = (ceylon.language.model.declaration.AliasDeclaration) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }

    @Override
    public String toString() {
        return "alias "+super.toString();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
