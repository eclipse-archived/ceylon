package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedHashMap;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.meta.declaration.ClassOrInterfaceDeclaration;
import ceylon.language.meta.declaration.OpenClassOrInterfaceType$impl;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.OpenType$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeClassOrInterfaceType
    implements ceylon.language.meta.declaration.OpenClassOrInterfaceType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeClassOrInterfaceType.class);
    
    private volatile boolean initialised;
    final com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType;
    protected com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassOrInterface declaration;
    protected ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.declaration.OpenType> typeArguments;
    protected ceylon.language.meta.declaration.OpenClassType superclass;
    protected Sequential<ceylon.language.meta.declaration.OpenInterfaceType> interfaces;
    
    FreeClassOrInterfaceType(com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType){
        this.producedType = producedType;
    }

    @Override
    @Ignore
    public OpenType$impl $ceylon$language$meta$declaration$OpenType$impl() {
        return null;
    }

    @Override
    @Ignore
    public OpenClassOrInterfaceType$impl $ceylon$language$meta$declaration$OpenClassOrInterfaceType$impl() {
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
        java.util.Map<ceylon.language.meta.declaration.TypeParameter, ceylon.language.meta.declaration.OpenType> typeArguments 
            = new LinkedHashMap<ceylon.language.meta.declaration.TypeParameter, ceylon.language.meta.declaration.OpenType>();
        Iterator<? extends ceylon.language.meta.declaration.TypeParameter> typeParameters = declaration.getTypeParameterDeclarations().iterator();
        Object it;
        java.util.Map<com.redhat.ceylon.compiler.typechecker.model.TypeParameter, com.redhat.ceylon.compiler.typechecker.model.ProducedType> ptArguments 
            = producedType.getTypeArguments();
        while((it = typeParameters.next()) != finished_.get_()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter tp = (com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter) it;
            com.redhat.ceylon.compiler.typechecker.model.TypeParameter tpDecl = (com.redhat.ceylon.compiler.typechecker.model.TypeParameter) tp.declaration;
            com.redhat.ceylon.compiler.typechecker.model.ProducedType ptArg = ptArguments.get(tpDecl);
            OpenType ptArgWrapped = Metamodel.getMetamodel(ptArg);
            typeArguments.put(tp, ptArgWrapped);
        }
        this.typeArguments = new InternalMap<ceylon.language.meta.declaration.TypeParameter, 
                                             ceylon.language.meta.declaration.OpenType>(ceylon.language.meta.declaration.TypeParameter.$TypeDescriptor, 
                                                                                             ceylon.language.meta.declaration.OpenType.$TypeDescriptor, 
                                                                                             typeArguments);
        
        com.redhat.ceylon.compiler.typechecker.model.ProducedType superType = decl.getExtendedType();
        if(superType != null){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType superTypeResolved = superType.substitute(producedType.getTypeArguments());
            this.superclass = (ceylon.language.meta.declaration.OpenClassType) Metamodel.getMetamodel(superTypeResolved);
        }
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> satisfiedTypes = decl.getSatisfiedTypes();
        ceylon.language.meta.declaration.OpenInterfaceType[] interfaces 
            = new ceylon.language.meta.declaration.OpenInterfaceType[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.ProducedType pt : satisfiedTypes){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType resolvedPt = pt.substitute(producedType.getTypeArguments());
            interfaces[i++] = (ceylon.language.meta.declaration.OpenInterfaceType) 
                    Metamodel.getMetamodel(resolvedPt);
        }
        this.interfaces = Util.sequentialInstance(ceylon.language.meta.declaration.OpenInterfaceType.$TypeDescriptor, interfaces);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,ceylon.language.meta.declaration::OpenType>")
    public Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends OpenType> getTypeArguments() {
        return typeArguments;
    }

    @Override
    public ClassOrInterfaceDeclaration getDeclaration() {
        checkInit();
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::OpenInterfaceType>")
    public Sequential<? extends ceylon.language.meta.declaration.OpenInterfaceType> getSatisfiedTypes() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::OpenClassType|ceylon.language::Null")
    public ceylon.language.meta.declaration.OpenClassType getExtendedType() {
        checkInit();
        return superclass;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaration().hashCode();
        result = 37 * result + getTypeArguments().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.declaration.OpenClassOrInterfaceType == false)
            return false;
        ceylon.language.meta.declaration.OpenClassOrInterfaceType other = (ceylon.language.meta.declaration.OpenClassOrInterfaceType) obj;
        if(!getDeclaration().equals(other.getDeclaration()))
            return false;
        return getTypeArguments().equals(other.getTypeArguments());
    }

    @Override
    public String toString() {
        String prefix = "";
        if(declaration instanceof ceylon.language.meta.declaration.ClassDeclaration)
            prefix = "class ";
        else if(declaration instanceof ceylon.language.meta.declaration.InterfaceDeclaration)
            prefix = "interface ";
        else if(declaration instanceof ceylon.language.meta.declaration.AliasDeclaration)
            prefix = "alias ";
        return prefix+Metamodel.toTypeString(getDeclaration(), getTypeArguments());
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
