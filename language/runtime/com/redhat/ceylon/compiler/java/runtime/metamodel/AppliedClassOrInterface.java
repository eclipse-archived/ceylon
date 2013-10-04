package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.model.ClassOrInterface$impl;
import ceylon.language.meta.model.Member;
import ceylon.language.meta.model.Model$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    })
public abstract class AppliedClassOrInterface<Type> 
    implements ceylon.language.meta.model.ClassOrInterface<Type>, ReifiedType {

    private volatile boolean initialised;
    final com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType;
    protected com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassOrInterface declaration;
    protected ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> typeArguments;
    protected ceylon.language.meta.model.ClassModel<? extends Object, ? super Sequential<? extends Object>> superclass;
    protected Sequential<ceylon.language.meta.model.InterfaceModel<? extends Object>> interfaces;
    @Ignore
    protected final TypeDescriptor $reifiedType;
    
    AppliedClassOrInterface(@Ignore TypeDescriptor $reifiedType, com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType){
        this.producedType = producedType;
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(producedType);
    }

    @Override
    @Ignore
    public ceylon.language.meta.model.Type$impl<Type> $ceylon$language$meta$model$Type$impl() {
        return null;
    }

    @Override
    @Ignore
    public ceylon.language.meta.model.Generic$impl $ceylon$language$meta$model$Generic$impl() {
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$meta$model$Model$impl() {
        return null;
    }
    
    @Override
    @Ignore
    public ClassOrInterface$impl<Type> $ceylon$language$meta$model$ClassOrInterface$impl() {
        return null;
    }

    protected void checkInit(){
        if(!initialised){
            synchronized(Metamodel.getLock()){
                if(!initialised){
                    init();
                    initialised = true;
                }
            }
        }
    }
    
    protected void init() {
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface decl = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) producedType.getDeclaration();
        this.declaration = (FreeClassOrInterface) Metamodel.getOrCreateMetamodel(decl);
        this.typeArguments = Metamodel.getTypeArguments(declaration, producedType);
        
        com.redhat.ceylon.compiler.typechecker.model.ProducedType superType = decl.getExtendedType();
        if(superType != null){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType superTypeResolved = superType.substitute(producedType.getTypeArguments());
            this.superclass = (ceylon.language.meta.model.ClassModel) Metamodel.getAppliedMetamodel(superTypeResolved);
        }
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes = decl.getSatisfiedTypes();
        ceylon.language.meta.model.InterfaceModel<?>[] interfaces = new ceylon.language.meta.model.InterfaceModel[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType resolvedPt = pt.substitute(producedType.getTypeArguments());
            interfaces[i++] = (ceylon.language.meta.model.InterfaceModel<?>) Metamodel.getAppliedMetamodel(resolvedPt);
        }
        this.interfaces = Util.sequentialInstance(TypeDescriptor.klass(ceylon.language.meta.model.InterfaceModel.class, ceylon.language.Anything.$TypeDescriptor), interfaces);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> getTypeArguments() {
        return typeArguments;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::ClassOrInterfaceDeclaration")
    public ceylon.language.meta.declaration.ClassOrInterfaceDeclaration getDeclaration() {
        checkInit();
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::InterfaceModel<ceylon.language::Anything>>")
    public Sequential<? extends ceylon.language.meta.model.InterfaceModel<? extends Object>> getSatisfiedTypes() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::ClassModel<ceylon.language::Anything,ceylon.language::Nothing>|ceylon.language::Null")
    public ceylon.language.meta.model.ClassModel<? extends Object, ? extends Object> getExtendedType() {
        checkInit();
        return superclass;
    }

    @Ignore
    @Override
    public <SubType, Type, Arguments extends Sequential<? extends Object>>
    ceylon.language.meta.model.Method<SubType, Type, Arguments> getMethod(@Ignore TypeDescriptor $reifiedSubType, 
                                                                         @Ignore TypeDescriptor $reifiedType, 
                                                                         @Ignore TypeDescriptor $reifiedArguments, 
                                                                         String name){
        
        return getMethod($reifiedSubType, $reifiedType, $reifiedArguments, name, (Sequential)empty_.get_());
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @TypeInfo("ceylon.language.meta.model::Method<SubType,Type,Arguments>|ceylon.language::Null")
    public <SubType, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.meta.model.Method<SubType, Type, Arguments> getMethod(@Ignore TypeDescriptor $reifiedSubType, 
                                                                             @Ignore TypeDescriptor $reifiedType, 
                                                                             @Ignore TypeDescriptor $reifiedArguments, 
                                                                             String name, 
                                                                             @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeFunction method = declaration.findMethod(name);
        if(method == null)
            return null;
        return method.<SubType, Type, Arguments>getAppliedMethod(this.$reifiedType, $reifiedType, $reifiedArguments, types, this);
    }

    @Ignore
    @Override
    public <SubType, Kind extends ceylon.language.meta.model.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.meta.model.Member<SubType, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedSubType, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name){
        
        return getClassOrInterface($reifiedSubType, $reifiedKind, name, (Sequential)empty_.get_());
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.model::ClassOrInterface<ceylon.language::Anything,ceylon.language::Nothing>")
    })
    @TypeInfo("ceylon.language.meta.model::Member<SubType,Kind>|ceylon.language::Null")
    public <SubType, Kind extends ceylon.language.meta.model.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.meta.model.Member<SubType, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedSubType, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name, 
                                                                            @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeClassOrInterface type = declaration.findType(name);
        if(type == null)
            return null;
        Member<SubType, Kind> member = type.getAppliedClassOrInterface(this.$reifiedType, $reifiedKind, types, (AppliedClassOrInterface<SubType>)this);

        // This is all very ugly but we're trying to make it cheaper and friendlier than just checking the full type and showing
        // implementation types to the user, such as AppliedMemberClass
        TypeDescriptor actualReifiedContainer;
        TypeDescriptor actualKind;
        
        if(member instanceof AppliedMemberClass){
            actualReifiedContainer = ((AppliedMemberClass)member).$reifiedContainer;
            actualKind = TypeDescriptor.klass(ceylon.language.meta.model.Class.class,
                    ((AppliedMemberClass) member).$reifiedType, 
                    ((AppliedMemberClass) member).$reifiedArguments);
        }else{
            actualReifiedContainer = ((AppliedMemberInterface)member).$reifiedContainer;
            actualKind = TypeDescriptor.klass(ceylon.language.meta.model.Interface.class,
                    ((AppliedMemberInterface) member).$reifiedType);
        }
        
        Metamodel.checkReifiedTypeArgument("getClassOrInterface", "Member<$1,$2>&$2", 
                Variance.IN, Metamodel.getProducedType(actualReifiedContainer), $reifiedSubType, 
                Variance.OUT, Metamodel.getProducedType(actualKind), $reifiedKind);

        return member;
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Type")
    })
    @TypeInfo("ceylon.language.meta.model::Attribute<Container,Type>|ceylon.language::Null")
    public <Container, Type>
        ceylon.language.meta.model.Attribute<Container, Type> getAttribute(@Ignore TypeDescriptor $reifiedContainer, 
                                                                        @Ignore TypeDescriptor $reifiedType, 
                                                                        String name) {
        
        checkInit();
        final FreeAttribute value = declaration.findValue(name);
        if(value == null)
            return null;
        com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration decl = (com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration) value.declaration;
        ProducedTypedReference typedReference = decl.getProducedTypedReference(producedType, Collections.<ProducedType>emptyList());
        TypeDescriptor reifiedValueType = Metamodel.getTypeDescriptorForProducedType(typedReference.getType());
        Metamodel.checkReifiedTypeArgument("getAttribute", "Attribute<$1,$2>", 
                Variance.IN, producedType, $reifiedContainer,
                Variance.OUT, typedReference.getType(), $reifiedType);
        return AppliedAttribute.instance(this.$reifiedType, reifiedValueType, value, typedReference, decl, this);
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }

    @Override
    public boolean isTypeOf(@TypeInfo("ceylon.language::Anything") Object instance){
        return Metamodel.isTypeOf(producedType, instance);
    }
    
    @Override
    public boolean isSuperTypeOf(@TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>") ceylon.language.meta.model.Type<? extends Object> type){
        return Metamodel.isSuperTypeOf(producedType, type);
    }
    
    @Override
    public boolean isSubTypeOf(@TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>") ceylon.language.meta.model.Type<? extends Object> type){
        return Metamodel.isSubTypeOf(producedType, type);
    }

    @Override
    public boolean isExactly(@TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>") ceylon.language.meta.model.Type<? extends Object> type){
        return Metamodel.isExactly(producedType, type);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedClassOrInterface.class, $reifiedType);
    }
}
