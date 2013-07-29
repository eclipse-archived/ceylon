package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.model.ClassOrInterface$impl;
import ceylon.language.model.Model$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.InternalMap;
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
public class AppliedClassOrInterface<Type> 
    implements ceylon.language.model.ClassOrInterface<Type>, ReifiedType {

    private volatile boolean initialised;
    final com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType;
    protected com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassOrInterface declaration;
    protected ceylon.language.Map<? extends ceylon.language.model.declaration.TypeParameter, ? extends ceylon.language.model.Type> typeArguments;
    protected ceylon.language.model.Class<? extends Object, ? super Sequential<? extends Object>> superclass;
    protected Sequential<ceylon.language.model.Interface<? extends Object>> interfaces;
    @Ignore
    protected final TypeDescriptor $reifiedType;
    
    @Override
    public String toString() {
        return producedType.getProducedTypeName();
    }

    AppliedClassOrInterface(@Ignore TypeDescriptor $reifiedType, com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType){
        this.producedType = producedType;
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(producedType);
    }

    @Override
    @Ignore
    public ceylon.language.model.Type$impl $ceylon$language$model$Type$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$model$Model$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @Ignore
    public ClassOrInterface$impl<Type> $ceylon$language$model$ClassOrInterface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    protected void checkInit(){
        if(!initialised){
            // FIXME: lock on model loader?
            synchronized(this){
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
        java.util.Map<ceylon.language.model.declaration.TypeParameter, ceylon.language.model.Type> typeArguments 
            = new LinkedHashMap<ceylon.language.model.declaration.TypeParameter, ceylon.language.model.Type>();
        Iterator<? extends ceylon.language.model.declaration.TypeParameter> typeParameters = declaration.getTypeParameterDeclarations().iterator();
        Object it;
        java.util.Map<com.redhat.ceylon.compiler.typechecker.model.TypeParameter, com.redhat.ceylon.compiler.typechecker.model.ProducedType> ptArguments 
            = producedType.getTypeArguments();
        while((it = typeParameters.next()) != finished_.$get()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter tp = (com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter) it;
            com.redhat.ceylon.compiler.typechecker.model.TypeParameter tpDecl = (com.redhat.ceylon.compiler.typechecker.model.TypeParameter) tp.declaration;
            com.redhat.ceylon.compiler.typechecker.model.ProducedType ptArg = ptArguments.get(tpDecl);
            ceylon.language.model.Type ptArgWrapped = Metamodel.getAppliedMetamodel(ptArg);
            typeArguments.put(tp, ptArgWrapped);
        }
        this.typeArguments = new InternalMap<ceylon.language.model.declaration.TypeParameter, 
                                             ceylon.language.model.Type>(ceylon.language.model.declaration.TypeParameter.$TypeDescriptor, 
                                                                                    ceylon.language.model.Type.$TypeDescriptor, 
                                                                                    typeArguments);
        
        com.redhat.ceylon.compiler.typechecker.model.ProducedType superType = decl.getExtendedType();
        if(superType != null){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType superTypeResolved = superType.substitute(producedType.getTypeArguments());
            this.superclass = (ceylon.language.model.Class) Metamodel.getAppliedMetamodel(superTypeResolved);
        }
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes = decl.getSatisfiedTypes();
        ceylon.language.model.Interface[] interfaces = new ceylon.language.model.Interface[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType resolvedPt = pt.substitute(producedType.getTypeArguments());
            interfaces[i++] = (ceylon.language.model.Interface) Metamodel.getAppliedMetamodel(resolvedPt);
        }
        // FIXME: reified type is wrong here and most likely in ClassOrInterfaceType too
        this.interfaces = (Sequential)Util.sequentialInstance(FreeClassOrInterface.$InterfacesTypeDescriptor, interfaces);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.model.declaration::TypeParameter,ceylon.language.model::Type>")
    public Map<? extends ceylon.language.model.declaration.TypeParameter, ? extends ceylon.language.model.Type> getTypeArguments() {
        return typeArguments;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::ClassOrInterfaceDeclaration")
    public ceylon.language.model.declaration.ClassOrInterfaceDeclaration getDeclaration() {
        checkInit();
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Interface<ceylon.language::Anything>>")
    public Sequential<? extends ceylon.language.model.Interface<? extends Object>> getInterfaces() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.model::Class<ceylon.language::Anything,ceylon.language::Nothing>|ceylon.language::Null")
    public ceylon.language.model.Class<? extends Object, ? extends Object> getSuperclass() {
        checkInit();
        return superclass;
    }

    @Ignore
    @Override
    public Sequential<? extends ceylon.language.model.Type> getMethod$types(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                @Ignore TypeDescriptor $reifiedType, 
                                                                                @Ignore TypeDescriptor $reifiedArguments, 
                                                                                String name){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public <SubType, Type, Arguments extends Sequential<? extends Object>>
    ceylon.language.model.Method<SubType, Type, Arguments> getMethod(@Ignore TypeDescriptor $reifiedSubType, 
                                                                         @Ignore TypeDescriptor $reifiedType, 
                                                                         @Ignore TypeDescriptor $reifiedArguments, 
                                                                         String name){
        
        return getMethod($reifiedSubType, $reifiedType, $reifiedArguments, name, getMethod$types($reifiedSubType, $reifiedType, $reifiedArguments, name));
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @TypeInfo("ceylon.language.model::Method<SubType,Type,Arguments>|ceylon.language::Null")
    public <SubType, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.model.Method<SubType, Type, Arguments> getMethod(@Ignore TypeDescriptor $reifiedSubType, 
                                                                             @Ignore TypeDescriptor $reifiedType, 
                                                                             @Ignore TypeDescriptor $reifiedArguments, 
                                                                             String name, 
                                                                             @Name("types") @Sequenced Sequential<? extends ceylon.language.model.Type> types) {
        
        checkInit();
        final FreeFunction method = declaration.findMethod(name);
        if(method == null)
            return null;
        return method.<SubType, Type, Arguments>getAppliedMethod($reifiedSubType, $reifiedType, $reifiedArguments, types, this);
    }

    @Ignore
    @Override
    public Sequential<? extends ceylon.language.model.Type> getClassOrInterface$types(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                                 @Ignore TypeDescriptor $reifiedKind, 
                                                                                                 String name){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public <SubType, Kind extends ceylon.language.model.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.model.Member<SubType, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedSubType, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name){
        
        return getClassOrInterface($reifiedSubType, $reifiedKind, name, getClassOrInterface$types($reifiedSubType, $reifiedKind, name));
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.model::ClassOrInterface<ceylon.language::Anything,ceylon.language::Nothing>")
    })
    @TypeInfo("ceylon.language.model::Member<SubType,Kind>|ceylon.language::Null")
    public <SubType, Kind extends ceylon.language.model.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.model.Member<SubType, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedSubType, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name, 
                                                                            @Name("types") @Sequenced Sequential<? extends ceylon.language.model.Type> types) {
        
        checkInit();
        final FreeClassOrInterface type = declaration.findType(name);
        if(type == null)
            return null;
        return type.getAppliedClassOrInterface($reifiedSubType, $reifiedKind, types, (AppliedClassOrInterface<SubType>)this);
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type")
    })
    @TypeInfo("ceylon.language.model::Attribute<SubType,Type>|ceylon.language::Null")
    public <SubType, Type>
        ceylon.language.model.Attribute<SubType, Type> getAttribute(@Ignore TypeDescriptor $reifiedSubType, 
                                                                        @Ignore TypeDescriptor $reifiedType, 
                                                                        String name) {
        
        checkInit();
        final FreeAttribute value = declaration.findValue(name);
        if(value == null)
            return null;
        com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration decl = (com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration) value.declaration;
        ProducedTypedReference typedReference = decl.getProducedTypedReference(producedType, Collections.<ProducedType>emptyList());
        TypeDescriptor reifiedValueType = Metamodel.getTypeDescriptorForProducedType(typedReference.getType());
        return AppliedAttribute.instance($reifiedSubType, reifiedValueType, value, typedReference, decl, this);
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type")
    })
    @TypeInfo("ceylon.language.model::VariableAttribute<SubType,Type>|ceylon.language::Null")
    public <SubType, Type>
        ceylon.language.model.VariableAttribute<SubType, Type> getVariableAttribute(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                        @Ignore TypeDescriptor $reifiedType, 
                                                                                        String name) {
        return (ceylon.language.model.VariableAttribute<SubType, Type>)getAttribute($reifiedSubType, $reifiedType, name);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(AppliedClassOrInterface.class, $reifiedType);
    }
}
