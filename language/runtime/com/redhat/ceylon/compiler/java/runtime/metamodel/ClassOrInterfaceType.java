package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedHashMap;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.metamodel.ClassOrInterfaceType$impl;
import ceylon.language.metamodel.ProducedType;
import ceylon.language.metamodel.ProducedType$impl;

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

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    })
public class ClassOrInterfaceType
    implements ceylon.language.metamodel.ClassOrInterfaceType, ReifiedType {

    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(ClassOrInterfaceType.class);
    
    private volatile boolean initialised;
    final com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType;
    protected com.redhat.ceylon.compiler.java.runtime.metamodel.ClassOrInterface declaration;
    protected ceylon.language.Map<? extends ceylon.language.metamodel.TypeParameter, ? extends ceylon.language.metamodel.ProducedType> typeArguments;
    protected ceylon.language.metamodel.ClassType superclass;
    protected Sequential<ceylon.language.metamodel.InterfaceType> interfaces;
    
    ClassOrInterfaceType(com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType){
        this.producedType = producedType;
    }

    @Override
    @Ignore
    public ProducedType$impl $ceylon$language$metamodel$ProducedType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public ClassOrInterfaceType$impl $ceylon$language$metamodel$ClassOrInterfaceType$impl() {
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
        java.util.Map<ceylon.language.metamodel.TypeParameter, ceylon.language.metamodel.ProducedType> typeArguments 
            = new LinkedHashMap<ceylon.language.metamodel.TypeParameter, ceylon.language.metamodel.ProducedType>();
        Iterator<? extends ceylon.language.metamodel.TypeParameter> typeParameters = declaration.getTypeParameters().iterator();
        Object it;
        java.util.Map<com.redhat.ceylon.compiler.typechecker.model.TypeParameter, com.redhat.ceylon.compiler.typechecker.model.ProducedType> ptArguments 
            = producedType.getTypeArguments();
        while((it = typeParameters.next()) != finished_.getFinished$()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.TypeParameter tp = (com.redhat.ceylon.compiler.java.runtime.metamodel.TypeParameter) it;
            com.redhat.ceylon.compiler.typechecker.model.TypeParameter tpDecl = (com.redhat.ceylon.compiler.typechecker.model.TypeParameter) tp.declaration;
            com.redhat.ceylon.compiler.typechecker.model.ProducedType ptArg = ptArguments.get(tpDecl);
            ProducedType ptArgWrapped = Metamodel.getMetamodel(ptArg);
            typeArguments.put(tp, ptArgWrapped);
        }
        this.typeArguments = new InternalMap<ceylon.language.metamodel.TypeParameter, 
                                             ceylon.language.metamodel.ProducedType>(ceylon.language.metamodel.TypeParameter.$TypeDescriptor, 
                                                                                     ceylon.language.metamodel.ProducedType.$TypeDescriptor, 
                                                                                     typeArguments);
        
        com.redhat.ceylon.compiler.typechecker.model.ProducedType superType = decl.getExtendedType();
        if(superType != null){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType superTypeResolved = superType.substitute(producedType.getTypeArguments());
            this.superclass = (ceylon.language.metamodel.ClassType) Metamodel.getMetamodel(superTypeResolved);
        }
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes = decl.getSatisfiedTypes();
        ceylon.language.metamodel.InterfaceType[] interfaces = new ceylon.language.metamodel.InterfaceType[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType resolvedPt = pt.substitute(producedType.getTypeArguments());
            interfaces[i++] = (ceylon.language.metamodel.InterfaceType) Metamodel.getMetamodel(resolvedPt);
        }
        this.interfaces = (Sequential)Util.sequentialInstance(ClassOrInterface.$InterfacesTypeDescriptor, interfaces);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.metamodel::TypeParameter,ceylon.language.metamodel::ProducedType>")
    public Map<? extends ceylon.language.metamodel.TypeParameter, ? extends ProducedType> getTypeArguments() {
        return typeArguments;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::ClassOrInterface")
    public ceylon.language.metamodel.ClassOrInterface getDeclaration() {
        checkInit();
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::InterfaceType>")
    public Sequential<? extends ceylon.language.metamodel.InterfaceType> getInterfaces() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::ClassType|ceylon.language::Null")
    public ceylon.language.metamodel.ClassType getSuperclass() {
        checkInit();
        return superclass;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
