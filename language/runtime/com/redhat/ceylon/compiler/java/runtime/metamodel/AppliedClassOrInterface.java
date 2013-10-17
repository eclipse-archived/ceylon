package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.model.ClassOrInterface$impl;
import ceylon.language.meta.model.IncompatibleTypeException;
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

@Ceylon(major = 6)
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
    
    @SuppressWarnings("unchecked")
    protected void init() {
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface decl = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) producedType.getDeclaration();
        this.declaration = (FreeClassOrInterface) Metamodel.getOrCreateMetamodel(decl);
        this.typeArguments = Metamodel.getTypeArguments(declaration, producedType);
        
        com.redhat.ceylon.compiler.typechecker.model.ProducedType superType = decl.getExtendedType();
        if(superType != null){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType superTypeResolved = superType.substitute(producedType.getTypeArguments());
            this.superclass = (ceylon.language.meta.model.ClassModel<?,? super Sequential<? extends Object>>) Metamodel.getAppliedMetamodel(superTypeResolved);
        }
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes = decl.getSatisfiedTypes();
        ceylon.language.meta.model.InterfaceModel<?>[] interfaces = new ceylon.language.meta.model.InterfaceModel[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType resolvedPt = pt.substitute(producedType.getTypeArguments());
            interfaces[i++] = (ceylon.language.meta.model.InterfaceModel<?>) Metamodel.getAppliedMetamodel(resolvedPt);
        }
        this.interfaces = Util.sequentialInstance(TypeDescriptor.klass(ceylon.language.meta.model.InterfaceModel.class, ceylon.language.Anything.$TypeDescriptor$), interfaces);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,ceylon.language.meta.model::Type<ceylon.language::Anything>>")
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
    public ceylon.language.meta.model.ClassModel<? extends Object, ? super Sequential<? extends Object>> getExtendedType() {
        checkInit();
        return superclass;
    }

    @SuppressWarnings({ "hiding", "unchecked" })
    @Ignore
    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
    ceylon.language.meta.model.Method<Container, Type, Arguments> getMethod(@Ignore TypeDescriptor $reifiedContainer, 
                                                                         @Ignore TypeDescriptor $reifiedType, 
                                                                         @Ignore TypeDescriptor $reifiedArguments, 
                                                                         String name){
        
        return getMethod($reifiedContainer, $reifiedType, $reifiedArguments, name, 
                (Sequential<? extends ceylon.language.meta.model.Type<?>>)empty_.get_());
    }

    @SuppressWarnings("hiding")
    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @TypeInfo("ceylon.language.meta.model::Method<Container,Type,Arguments>|ceylon.language::Null")
    public <Container, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.meta.model.Method<Container, Type, Arguments> getMethod(@Ignore TypeDescriptor $reifiedContainer, 
                                                                             @Ignore TypeDescriptor $reifiedType, 
                                                                             @Ignore TypeDescriptor $reifiedArguments, 
                                                                             String name, 
                                                                             @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeFunction method = declaration.findMethod(name);
        if(method == null)
            return null;
        ceylon.language.meta.model.Type<Container> appliedContainer = getAppliedContainer($reifiedContainer, method);
        return method.memberApply($reifiedContainer, $reifiedType, $reifiedArguments, appliedContainer, types);
    }

    @SuppressWarnings({ "hiding", "unchecked" })
    @Ignore
    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
    ceylon.language.meta.model.Method<Container, Type, Arguments> getDeclaredMethod(@Ignore TypeDescriptor $reifiedContainer, 
                                                                         @Ignore TypeDescriptor $reifiedType, 
                                                                         @Ignore TypeDescriptor $reifiedArguments, 
                                                                         String name){
        
        return getDeclaredMethod($reifiedContainer, $reifiedType, $reifiedArguments, name, 
                (Sequential<? extends ceylon.language.meta.model.Type<?>>)empty_.get_());
    }

    @SuppressWarnings({ "unchecked", "hiding" })
    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @TypeInfo("ceylon.language.meta.model::Method<Container,Type,Arguments>|ceylon.language::Null")
    public <Container, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.meta.model.Method<Container, Type, Arguments> getDeclaredMethod(@Ignore TypeDescriptor $reifiedContainer, 
                                                                             @Ignore TypeDescriptor $reifiedType, 
                                                                             @Ignore TypeDescriptor $reifiedArguments, 
                                                                             String name, 
                                                                             @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeFunction method = declaration.findDeclaredMethod(name);
        if(method == null)
            return null;
        return method.memberApply($reifiedContainer, $reifiedType, $reifiedArguments, 
                (ceylon.language.meta.model.Type<Container>)this, types);
    }

    @SuppressWarnings("unchecked")
    @Ignore
    @Override
    public <Container, Kind extends ceylon.language.meta.model.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.meta.model.Member<Container, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name){
        
        return getClassOrInterface($reifiedContainer, $reifiedKind, name, 
                (Sequential<? extends ceylon.language.meta.model.Type<?>>)empty_.get_());
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.model::ClassOrInterface<ceylon.language::Anything,ceylon.language::Nothing>")
    })
    @TypeInfo("ceylon.language.meta.model::Member<Container,Kind>|ceylon.language::Null")
    public <Container, Kind extends ceylon.language.meta.model.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.meta.model.Member<Container, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name, 
                                                                            @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeClassOrInterface type = declaration.findType(name);
        return applyClassOrInterface($reifiedContainer, $reifiedKind, type, types);
    }

    @SuppressWarnings("unchecked")
    @Ignore
    @Override
    public <Container, Kind extends ceylon.language.meta.model.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.meta.model.Member<Container, Kind> getDeclaredClassOrInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name){
        
        return getDeclaredClassOrInterface($reifiedContainer, $reifiedKind, name, 
                (Sequential<? extends ceylon.language.meta.model.Type<?>>)empty_.get_());
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.model::ClassOrInterface<ceylon.language::Anything,ceylon.language::Nothing>")
    })
    @TypeInfo("ceylon.language.meta.model::Member<Container,Kind>|ceylon.language::Null")
    public <Container, Kind extends ceylon.language.meta.model.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.meta.model.Member<Container, Kind> getDeclaredClassOrInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name, 
                                                                            @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeClassOrInterface type = declaration.findDeclaredType(name);
        return applyClassOrInterface($reifiedContainer, $reifiedKind, type, types);
    }
    
    @SuppressWarnings("rawtypes")
    private <Container, Kind extends ceylon.language.meta.model.ClassOrInterface<? extends java.lang.Object>>
    ceylon.language.meta.model.Member<Container, Kind> applyClassOrInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                        @Ignore TypeDescriptor $reifiedKind, 
                                                                        FreeClassOrInterface type, 
                                                                        Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        if(type == null)
            return null;
        ceylon.language.meta.model.Type<Container> appliedContainer = getAppliedContainer($reifiedContainer, type);
        Member<Container, Kind> member = type.getAppliedClassOrInterface(this.$reifiedType, $reifiedKind, types, appliedContainer);

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
                Variance.IN, Metamodel.getProducedType(actualReifiedContainer), $reifiedContainer, 
                Variance.OUT, Metamodel.getProducedType(actualKind), $reifiedKind);

        return member;
    }

    @SuppressWarnings({ "hiding", "unchecked" })
    @Ignore
    @Override
    public <Container, Type, Arguments extends ceylon.language.Sequential<? extends java.lang.Object>>
        ceylon.language.meta.model.MemberClass<Container, Type, Arguments> $getClass(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedType, 
                                                                            @Ignore TypeDescriptor $reifiedArguments, 
                                                                            String name){
        
        return $getClass($reifiedContainer, $reifiedType, $reifiedArguments, name, 
                (Sequential<? extends ceylon.language.meta.model.Type<?>>)empty_.get_());
    }

    @SuppressWarnings("hiding")
    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @TypeInfo("ceylon.language.meta.model::MemberClass<Container,Type,Arguments>|ceylon.language::Null")
    public <Container, Type, Arguments extends ceylon.language.Sequential<? extends java.lang.Object>>
    ceylon.language.meta.model.MemberClass<Container, Type, Arguments> $getClass(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedType, 
                                                                            @Ignore TypeDescriptor $reifiedArguments, 
                                                                            String name, 
                                                                            @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeClassOrInterface type = declaration.findType(name);
        if(type instanceof FreeClass == false)
            throw new IncompatibleTypeException("Specified member is not a class: "+name);
        ceylon.language.meta.model.Type<Container> appliedContainer = getAppliedContainer($reifiedContainer, type);
        return ((FreeClass)type).memberClassApply($reifiedContainer, $reifiedType, $reifiedArguments, 
                                                  appliedContainer, types);
    }
    
    @SuppressWarnings({ "hiding", "unchecked" })
    @Ignore
    @Override
    public <Container, Type, Arguments extends ceylon.language.Sequential<? extends java.lang.Object>>
        ceylon.language.meta.model.MemberClass<Container, Type, Arguments> getDeclaredClass(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedType, 
                                                                            @Ignore TypeDescriptor $reifiedArguments, 
                                                                            String name){
        
        return getDeclaredClass($reifiedContainer, $reifiedType, $reifiedArguments, name, 
                (Sequential<? extends ceylon.language.meta.model.Type<?>>)empty_.get_());
    }

    @SuppressWarnings({ "unchecked", "hiding" })
    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @TypeInfo("ceylon.language.meta.model::MemberClass<Container,Type,Arguments>|ceylon.language::Null")
    public <Container, Type, Arguments extends ceylon.language.Sequential<? extends java.lang.Object>>
    ceylon.language.meta.model.MemberClass<Container, Type, Arguments> getDeclaredClass(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedType, 
                                                                            @Ignore TypeDescriptor $reifiedArguments, 
                                                                            String name, 
                                                                            @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeClassOrInterface type = declaration.findDeclaredType(name);
        if(type instanceof FreeClass == false)
            throw new IncompatibleTypeException("Specified member is not a class: "+name);
        return ((FreeClass)type).memberClassApply($reifiedContainer, $reifiedType, $reifiedArguments, 
                                                  (ceylon.language.meta.model.Type<Container>)this, types);
    }

    @SuppressWarnings({ "hiding", "unchecked" })
    @Ignore
    @Override
    public <Container, Type>
        ceylon.language.meta.model.MemberInterface<Container, Type> getInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedType, 
                                                                            String name){
        
        return getInterface($reifiedContainer, $reifiedType, name, 
                (Sequential<? extends ceylon.language.meta.model.Type<?>>)empty_.get_());
    }

    @SuppressWarnings({ "unchecked", "hiding" })
    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Type"),
    })
    @TypeInfo("ceylon.language.meta.model::MemberInterface<Container,Type>|ceylon.language::Null")
    public <Container, Type>
    ceylon.language.meta.model.MemberInterface<Container, Type> getInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedType, 
                                                                            String name, 
                                                                            @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeClassOrInterface type = declaration.findType(name);
        if(type instanceof FreeInterface == false)
            throw new IncompatibleTypeException("Specified member is not an interface: "+name);
        ceylon.language.meta.model.Type<Container> appliedContainer = getAppliedContainer($reifiedContainer, type);
        return (ceylon.language.meta.model.MemberInterface<Container, Type>) 
                type.memberApply($reifiedContainer, $reifiedType, appliedContainer);
    }

    @SuppressWarnings({ "hiding", "unchecked" })
    @Ignore
    @Override
    public <Container, Type>
        ceylon.language.meta.model.MemberInterface<Container, Type> getDeclaredInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedType, 
                                                                            String name){
        
        return getDeclaredInterface($reifiedContainer, $reifiedType, name, 
                (Sequential<? extends ceylon.language.meta.model.Type<?>>)empty_.get_());
    }

    @SuppressWarnings({ "unchecked", "hiding" })
    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Type"),
    })
    @TypeInfo("ceylon.language.meta.model::MemberInterface<Container,Type>|ceylon.language::Null")
    public <Container, Type>
    ceylon.language.meta.model.MemberInterface<Container, Type> getDeclaredInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                            @Ignore TypeDescriptor $reifiedType, 
                                                                            String name, 
                                                                            @Name("types") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        
        checkInit();
        final FreeClassOrInterface type = declaration.findDeclaredType(name);
        if(type instanceof FreeInterface == false)
            throw new IncompatibleTypeException("Specified member is not an interface: "+name);
        return (ceylon.language.meta.model.MemberInterface<Container, Type>) 
                type.memberApply($reifiedContainer, $reifiedType, 
                                 (ceylon.language.meta.model.Type<Container>)this);
    }

    @SuppressWarnings("hiding")
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
        final FreeValue value = declaration.findValue(name);
        if(value == null)
            return null;
        ceylon.language.meta.model.Type<Container> appliedContainer = getAppliedContainer($reifiedContainer, value);
        return value.<Container, Type>memberApply($reifiedContainer, $reifiedType, appliedContainer);
    }

    @SuppressWarnings("unchecked")
    private <Container> ceylon.language.meta.model.Type<Container> getAppliedContainer(@Ignore TypeDescriptor $reifiedContainer, 
            FreeNestableDeclaration decl) {
        FreeClassOrInterface valueContainer = (FreeClassOrInterface) decl.getContainer();
        if(valueContainer != declaration){
            ProducedType valueContainerType = this.producedType.getSupertype((com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration)valueContainer.declaration);
            return Metamodel.getAppliedMetamodel(valueContainerType);
        }else{
            return (ceylon.language.meta.model.Type<Container>) this;
        }
    }

    @SuppressWarnings({ "hiding", "unchecked" })
    @Override
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Type")
    })
    @TypeInfo("ceylon.language.meta.model::Attribute<Container,Type>|ceylon.language::Null")
    public <Container, Type>
        ceylon.language.meta.model.Attribute<Container, Type> getDeclaredAttribute(@Ignore TypeDescriptor $reifiedContainer, 
                                                                        @Ignore TypeDescriptor $reifiedType, 
                                                                        String name) {
        
        checkInit();
        final FreeValue value = declaration.findDeclaredValue(name);
        if(value == null)
            return null;
        return value.memberApply($reifiedContainer, $reifiedType, 
                (ceylon.language.meta.model.Type<Container>)this);
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
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AppliedClassOrInterface.class, $reifiedType);
    }
}
