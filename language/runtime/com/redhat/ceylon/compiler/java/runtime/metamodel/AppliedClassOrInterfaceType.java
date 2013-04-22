package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedHashMap;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.metamodel.AppliedClassOrInterfaceType$impl;
import ceylon.language.metamodel.AppliedProducedType;
import ceylon.language.metamodel.AppliedProducedType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    })
public class AppliedClassOrInterfaceType<Type> 
    implements ceylon.language.metamodel.AppliedClassOrInterfaceType<Type>, ReifiedType {

    private volatile boolean initialised;
    final com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType;
    protected com.redhat.ceylon.compiler.java.runtime.metamodel.ClassOrInterface declaration;
    protected ceylon.language.Map<? extends ceylon.language.metamodel.TypeParameter, ? extends ceylon.language.metamodel.AppliedProducedType> typeArguments;
    protected ceylon.language.metamodel.AppliedClassType<? extends Object, ? super Sequential<? extends Object>> superclass;
    protected Sequential<ceylon.language.metamodel.AppliedInterfaceType<? extends Object>> interfaces;
    
    AppliedClassOrInterfaceType(com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType){
        this.producedType = producedType;
    }

    @Override
    @Ignore
    public AppliedProducedType$impl $ceylon$language$metamodel$AppliedProducedType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AppliedClassOrInterfaceType$impl<Type> $ceylon$language$metamodel$AppliedClassOrInterfaceType$impl() {
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
        this.declaration = Metamodel.getOrCreateMetamodel(decl);
        java.util.Map<ceylon.language.metamodel.TypeParameter, ceylon.language.metamodel.AppliedProducedType> typeArguments 
            = new LinkedHashMap<ceylon.language.metamodel.TypeParameter, ceylon.language.metamodel.AppliedProducedType>();
        Iterator<? extends ceylon.language.metamodel.TypeParameter> typeParameters = declaration.getTypeParameters().iterator();
        Object it;
        java.util.Map<com.redhat.ceylon.compiler.typechecker.model.TypeParameter, com.redhat.ceylon.compiler.typechecker.model.ProducedType> ptArguments 
            = producedType.getTypeArguments();
        while((it = typeParameters.next()) != finished_.getFinished$()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.TypeParameter tp = (com.redhat.ceylon.compiler.java.runtime.metamodel.TypeParameter) it;
            com.redhat.ceylon.compiler.typechecker.model.TypeParameter tpDecl = (com.redhat.ceylon.compiler.typechecker.model.TypeParameter) tp.declaration;
            com.redhat.ceylon.compiler.typechecker.model.ProducedType ptArg = ptArguments.get(tpDecl);
            AppliedProducedType ptArgWrapped = Metamodel.getAppliedMetamodel(ptArg);
            typeArguments.put(tp, ptArgWrapped);
        }
        this.typeArguments = new InternalMap<ceylon.language.metamodel.TypeParameter, 
                                             ceylon.language.metamodel.AppliedProducedType>(ceylon.language.metamodel.TypeParameter.$TypeDescriptor, 
                                                                                            ceylon.language.metamodel.ProducedType.$TypeDescriptor, 
                                                                                            typeArguments);
        
        com.redhat.ceylon.compiler.typechecker.model.ProducedType superType = decl.getExtendedType();
        if(superType != null){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType superTypeResolved = superType.substitute(producedType.getTypeArguments());
            this.superclass = (ceylon.language.metamodel.AppliedClassType) Metamodel.getAppliedMetamodel(superTypeResolved);
        }
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes = decl.getSatisfiedTypes();
        ceylon.language.metamodel.AppliedInterfaceType[] interfaces = new ceylon.language.metamodel.AppliedInterfaceType[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType resolvedPt = pt.substitute(producedType.getTypeArguments());
            interfaces[i++] = (ceylon.language.metamodel.AppliedInterfaceType) Metamodel.getAppliedMetamodel(resolvedPt);
        }
        // FIXME: reified type is wrong here and most likely in ClassOrInterfaceType too
        this.interfaces = (Sequential)Util.sequentialInstance(ClassOrInterface.$InterfacesTypeDescriptor, interfaces);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.metamodel::TypeParameter,ceylon.language.metamodel::AppliedProducedType>")
    public Map<? extends ceylon.language.metamodel.TypeParameter, ? extends AppliedProducedType> getTypeArguments() {
        return typeArguments;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::ClassOrInterface")
    public ceylon.language.metamodel.ClassOrInterface getDeclaration() {
        checkInit();
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::AppliedInterfaceType<ceylon.language::Anything>>")
    public Sequential<? extends ceylon.language.metamodel.AppliedInterfaceType<? extends Object>> getInterfaces() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::AppliedClassType<ceylon.language::Anything,ceylon.language::Sequential<ceylon.language::Nothing>>|ceylon.language::Null")
    public ceylon.language.metamodel.AppliedClassType<? extends Object, ? super Sequential<? extends Object>> getSuperclass() {
        checkInit();
        return superclass;
    }

    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(AppliedClassOrInterfaceType.class, this.declaration.$getReifiedType());
    }
}
