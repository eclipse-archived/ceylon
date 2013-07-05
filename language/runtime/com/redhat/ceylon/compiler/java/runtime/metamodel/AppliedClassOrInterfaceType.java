package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedHashMap;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.ClassOrInterface$impl;
import ceylon.language.metamodel.DeclarationType$impl;

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

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    })
public class AppliedClassOrInterfaceType<Type> 
    implements ceylon.language.metamodel.ClassOrInterface<Type>, ReifiedType {

    private volatile boolean initialised;
    final com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType;
    protected com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassOrInterface declaration;
    protected ceylon.language.Map<? extends ceylon.language.metamodel.declaration.TypeParameter, ? extends ceylon.language.metamodel.Type> typeArguments;
    protected ceylon.language.metamodel.Class<? extends Object, ? super Sequential<? extends Object>> superclass;
    protected Sequential<ceylon.language.metamodel.Interface<? extends Object>> interfaces;
    @Ignore
    protected final TypeDescriptor $reifiedType;
    
    AppliedClassOrInterfaceType(@Ignore TypeDescriptor $reifiedType, com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType){
        this.producedType = producedType;
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(producedType);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Type$impl $ceylon$language$metamodel$Type$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public DeclarationType$impl $ceylon$language$metamodel$DeclarationType$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @Ignore
    public ClassOrInterface$impl<Type> $ceylon$language$metamodel$ClassOrInterface$impl() {
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
        java.util.Map<ceylon.language.metamodel.declaration.TypeParameter, ceylon.language.metamodel.Type> typeArguments 
            = new LinkedHashMap<ceylon.language.metamodel.declaration.TypeParameter, ceylon.language.metamodel.Type>();
        Iterator<? extends ceylon.language.metamodel.declaration.TypeParameter> typeParameters = declaration.getTypeParameterDeclarations().iterator();
        Object it;
        java.util.Map<com.redhat.ceylon.compiler.typechecker.model.TypeParameter, com.redhat.ceylon.compiler.typechecker.model.ProducedType> ptArguments 
            = producedType.getTypeArguments();
        while((it = typeParameters.next()) != finished_.$get()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter tp = (com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter) it;
            com.redhat.ceylon.compiler.typechecker.model.TypeParameter tpDecl = (com.redhat.ceylon.compiler.typechecker.model.TypeParameter) tp.declaration;
            com.redhat.ceylon.compiler.typechecker.model.ProducedType ptArg = ptArguments.get(tpDecl);
            ceylon.language.metamodel.Type ptArgWrapped = Metamodel.getAppliedMetamodel(ptArg);
            typeArguments.put(tp, ptArgWrapped);
        }
        this.typeArguments = new InternalMap<ceylon.language.metamodel.declaration.TypeParameter, 
                                             ceylon.language.metamodel.Type>(ceylon.language.metamodel.declaration.TypeParameter.$TypeDescriptor, 
                                                                                    ceylon.language.metamodel.Type.$TypeDescriptor, 
                                                                                    typeArguments);
        
        com.redhat.ceylon.compiler.typechecker.model.ProducedType superType = decl.getExtendedType();
        if(superType != null){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType superTypeResolved = superType.substitute(producedType.getTypeArguments());
            this.superclass = (ceylon.language.metamodel.Class) Metamodel.getAppliedMetamodel(superTypeResolved);
        }
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes = decl.getSatisfiedTypes();
        ceylon.language.metamodel.Interface[] interfaces = new ceylon.language.metamodel.Interface[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType resolvedPt = pt.substitute(producedType.getTypeArguments());
            interfaces[i++] = (ceylon.language.metamodel.Interface) Metamodel.getAppliedMetamodel(resolvedPt);
        }
        // FIXME: reified type is wrong here and most likely in ClassOrInterfaceType too
        this.interfaces = (Sequential)Util.sequentialInstance(FreeClassOrInterface.$InterfacesTypeDescriptor, interfaces);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.metamodel.declaration::TypeParameter,ceylon.language.metamodel::Type>")
    public Map<? extends ceylon.language.metamodel.declaration.TypeParameter, ? extends ceylon.language.metamodel.Type> getTypeArguments() {
        return typeArguments;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::ClassOrInterfaceDeclaration")
    public ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration getDeclaration() {
        checkInit();
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Interface<ceylon.language::Anything>>")
    public Sequential<? extends ceylon.language.metamodel.Interface<? extends Object>> getInterfaces() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Class<ceylon.language::Anything,ceylon.language::Nothing>|ceylon.language::Null")
    public ceylon.language.metamodel.Class<? extends Object, ? extends Object> getSuperclass() {
        checkInit();
        return superclass;
    }

    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.Type> getFunction$types(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                         @Ignore TypeDescriptor $reifiedKind, 
                                                                                         String name){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public <SubType, Kind extends ceylon.language.metamodel.Function>
        ceylon.language.metamodel.Member<SubType, Kind> getFunction(@Ignore TypeDescriptor $reifiedSubType, 
                                                                    @Ignore TypeDescriptor $reifiedKind, 
                                                                    String name){
        
        return getFunction($reifiedSubType, $reifiedKind, name, getFunction$types($reifiedSubType, $reifiedKind, name));
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Function<ceylon.language::Anything,ceylon.language::Nothing>")
    })
    @TypeInfo("ceylon.language.metamodel::Member<SubType,Kind>|ceylon.language::Null")
    public <SubType, Kind extends ceylon.language.metamodel.Function>
        ceylon.language.metamodel.Member<SubType, Kind> getFunction(@Ignore TypeDescriptor $reifiedSubType, 
                                                                    @Ignore TypeDescriptor $reifiedKind, 
                                                                    String name, 
                                                                    @Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types) {
        
        checkInit();
        final FreeFunction method = declaration.findMethod(name);
        if(method == null)
            return null;
        return method.<SubType,Kind>getAppliedFunction($reifiedSubType, $reifiedKind, types, (AppliedClassOrInterfaceType<SubType>)this);
    }

    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.Type> getClassOrInterface$types(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                                 @Ignore TypeDescriptor $reifiedKind, 
                                                                                                 String name){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public <SubType, Kind extends ceylon.language.metamodel.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.metamodel.Member<SubType, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedSubType, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name){
        
        return getClassOrInterface($reifiedSubType, $reifiedKind, name, getClassOrInterface$types($reifiedSubType, $reifiedKind, name));
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::ClassOrInterface<ceylon.language::Anything,ceylon.language::Nothing>")
    })
    @TypeInfo("ceylon.language.metamodel::Member<SubType,Kind>|ceylon.language::Null")
    public <SubType, Kind extends ceylon.language.metamodel.ClassOrInterface<? extends java.lang.Object>>
        ceylon.language.metamodel.Member<SubType, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedSubType, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            String name, 
                                                                            @Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types) {
        
        checkInit();
        final FreeClassOrInterface type = declaration.findType(name);
        if(type == null)
            return null;
        return type.getAppliedClassOrInterface($reifiedSubType, $reifiedKind, types, (AppliedClassOrInterfaceType<SubType>)this);
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Attribute<ceylon.language::Anything>")
    })
    @TypeInfo("ceylon.language.metamodel::Member<SubType,Kind>|ceylon.language::Null")
    public <SubType, Kind extends ceylon.language.metamodel.Attribute<? extends Object>>
        ceylon.language.metamodel.Member<SubType, Kind> getAttribute(@Ignore TypeDescriptor $reifiedSubType, 
                                                                     @Ignore TypeDescriptor $reifiedKind, 
                                                                     String name) {
        
        checkInit();
        final FreeAttribute value = declaration.findValue(name);
        if(value == null)
            return null;
        final com.redhat.ceylon.compiler.typechecker.model.Value decl = (com.redhat.ceylon.compiler.typechecker.model.Value) value.declaration;
        final ProducedType valueType = decl.getType().substitute(producedType.getTypeArguments());
        
        return new AppliedMember<SubType, Kind>($reifiedSubType, $reifiedKind/*, (AppliedClassOrInterfaceType<SubType>) this*/){
            @Override
            protected Kind bindTo(Object instance) {
                return (Kind) (decl.isVariable() ? new AppliedVariable(value, valueType, instance) : new AppliedValue(value, valueType, instance));
            }
        };
    }
    
    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(AppliedClassOrInterfaceType.class, $reifiedType);
    }
}
