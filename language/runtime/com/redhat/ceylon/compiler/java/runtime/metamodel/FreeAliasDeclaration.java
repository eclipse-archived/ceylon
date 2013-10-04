package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.meta.declaration.AliasDeclaration$impl;
import ceylon.language.meta.declaration.GenericDeclaration$impl;
import ceylon.language.meta.declaration.OpenType;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public class FreeAliasDeclaration extends FreeNestableDeclaration 
    implements ceylon.language.meta.declaration.AliasDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeAliasDeclaration.class);

    private OpenType openType;

    private boolean initialised = false;

    private Sequential<? extends ceylon.language.meta.declaration.TypeParameter> typeParameters;

    private OpenType extendedType;
    
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
        
        this.extendedType = Metamodel.getMetamodel(declaration.getExtendedType());
    }

    @Override
    @Ignore
    public AliasDeclaration$impl $ceylon$language$meta$declaration$AliasDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public GenericDeclaration$impl $ceylon$language$meta$declaration$GenericDeclaration$impl() {
        return null;
    }

    @Override
    public OpenType getOpenType() {
        return openType;
    }

    @Override
    public OpenType getExtendedType() {
        checkInit();
        return extendedType;
    }
    
    @Override
    public ceylon.language.meta.declaration.TypeParameter getTypeParameterDeclaration(@Name("name") @TypeInfo("ceylon.language::String") String name) {
        return Metamodel.findDeclarationByName(getTypeParameterDeclarations(), name);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::TypeParameter>")
    public Sequential<? extends ceylon.language.meta.declaration.TypeParameter> getTypeParameterDeclarations() {
        checkInit();
        return typeParameters;
    }

    /* FIXME: this is all too shaky wrt member types

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public <Type> ceylon.language.meta.model.Type<Type> apply(@Ignore TypeDescriptor $reifiedType){
        return apply($reifiedType, (Sequential)empty_.get_());
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<Type>")
    @TypeParameters({
        @TypeParameter("Type"),
    })
    public <Type extends Object> ceylon.language.meta.model.Type<Type> apply(@Ignore TypeDescriptor $reifiedType,
            @Name("typeArguments") @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(!getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a member declaration with no container type: use memberApply");
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(typeArguments);
        Metamodel.checkTypeArguments(null, declaration, producedTypes);
        ProducedReference producedReference = declaration.getProducedReference(null, producedTypes);
        final ProducedType appliedType = producedReference.getType();
        return Metamodel.getAppliedMetamodel(appliedType.resolveAliases());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Ignore
    @Override
    public <Container, Type extends Object>
        java.lang.Object memberApply(TypeDescriptor $reifiedContainer,
                                     TypeDescriptor $reifiedType,
                                     ceylon.language.meta.model.Type<? extends Container> containerType){
        
        return this.<Container, Type>memberApply($reifiedContainer,
                                                 $reifiedType,
                                                 containerType,
                                                 (Sequential)empty_.get_());
    }

    @TypeInfo("ceylon.language.meta.model::Member<Container,ceylon.language.meta.model::Type<Type>>&ceylon.language.meta.model::Type<Type>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Type"),
    })
    @Override
    public <Container, Type extends Object>
        java.lang.Object memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedType,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Container> containerType,
                @Name("typeArguments") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a toplevel declaration to a container type: use apply");
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(typeArguments);
        ProducedType qualifyingType = Metamodel.getModel(containerType);
        Metamodel.checkTypeArguments(qualifyingType, declaration, producedTypes);
        ProducedReference producedReference = declaration.getProducedReference(qualifyingType, producedTypes);
        final ProducedType appliedType = producedReference.getType();
        return Metamodel.getAppliedMetamodel(appliedType.resolveAliases());
    }
*/

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
        if(obj instanceof ceylon.language.meta.declaration.AliasDeclaration == false)
            return false;
        ceylon.language.meta.declaration.AliasDeclaration other = (ceylon.language.meta.declaration.AliasDeclaration) obj;
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
