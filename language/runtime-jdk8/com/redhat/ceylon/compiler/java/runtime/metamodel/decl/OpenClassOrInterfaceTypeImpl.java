package com.redhat.ceylon.compiler.java.runtime.metamodel.decl;

import java.util.LinkedHashMap;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.meta.declaration.ClassOrInterfaceDeclaration;
import ceylon.language.meta.declaration.OpenType;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class OpenClassOrInterfaceTypeImpl
    implements ceylon.language.meta.declaration.OpenClassOrInterfaceType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(OpenClassOrInterfaceTypeImpl.class);
    
    private volatile boolean initialised;
    public final com.redhat.ceylon.model.typechecker.model.Type producedType;
    protected com.redhat.ceylon.compiler.java.runtime.metamodel.decl.ClassOrInterfaceDeclarationImpl declaration;
    protected ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.declaration.OpenType> typeArguments;
    protected ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.Sequence<? extends Object>> typeArgumentWithVariances;
    protected ceylon.language.meta.declaration.OpenClassType superclass;
    protected Sequential<ceylon.language.meta.declaration.OpenInterfaceType> interfaces;
    
    OpenClassOrInterfaceTypeImpl(com.redhat.ceylon.model.typechecker.model.Type producedType){
        this.producedType = producedType;
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
        com.redhat.ceylon.model.typechecker.model.ClassOrInterface decl = (com.redhat.ceylon.model.typechecker.model.ClassOrInterface) producedType.getDeclaration();
        this.declaration = (ClassOrInterfaceDeclarationImpl) Metamodel.getOrCreateMetamodel(decl);
        
        java.util.Map<ceylon.language.meta.declaration.TypeParameter, ceylon.language.meta.declaration.OpenType> typeArguments 
            = new LinkedHashMap<ceylon.language.meta.declaration.TypeParameter, ceylon.language.meta.declaration.OpenType>();
        java.util.Map<ceylon.language.meta.declaration.TypeParameter, ceylon.language.Sequence<? extends Object>> typeArgumentWithVariances 
        = new LinkedHashMap<ceylon.language.meta.declaration.TypeParameter, ceylon.language.Sequence<? extends Object>>();
        
        Iterator<? extends ceylon.language.meta.declaration.TypeParameter> typeParameters = declaration.getTypeParameterDeclarations().iterator();
        Object it;
        java.util.Map<com.redhat.ceylon.model.typechecker.model.TypeParameter, com.redhat.ceylon.model.typechecker.model.Type> ptArguments 
            = producedType.getTypeArguments();
        java.util.Map<TypeParameter, SiteVariance> varianceOverrides = producedType.getVarianceOverrides();
        while((it = typeParameters.next()) != finished_.get_()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.decl.TypeParameterImpl tp = (com.redhat.ceylon.compiler.java.runtime.metamodel.decl.TypeParameterImpl) it;
            com.redhat.ceylon.model.typechecker.model.TypeParameter tpDecl = (com.redhat.ceylon.model.typechecker.model.TypeParameter) tp.declaration;
            com.redhat.ceylon.model.typechecker.model.Type ptArg = ptArguments.get(tpDecl);
            OpenType ptArgWrapped = Metamodel.getMetamodel(ptArg);
            typeArguments.put(tp, ptArgWrapped);
            
            ceylon.language.meta.declaration.Variance variance = Metamodel.modelVarianceToMetaModel(varianceOverrides, tpDecl);
            ceylon.language.Sequence<? extends Object> tuple = ceylon.language.Tuple.instance(Metamodel.TD_OpenTypeArgumentElement, new Object[]{ptArgWrapped, variance});
            typeArgumentWithVariances.put(tp, tuple);
        }
        this.typeArguments = new InternalMap<ceylon.language.meta.declaration.TypeParameter, 
                                             ceylon.language.meta.declaration.OpenType>(ceylon.language.meta.declaration.TypeParameter.$TypeDescriptor$, 
                                                                                        ceylon.language.meta.declaration.OpenType.$TypeDescriptor$, 
                                                                                        typeArguments);
        this.typeArgumentWithVariances = new InternalMap<ceylon.language.meta.declaration.TypeParameter, 
                                                         ceylon.language.Sequence<?>>(ceylon.language.meta.declaration.TypeParameter.$TypeDescriptor$, 
                                                                                      Metamodel.TD_OpenTypeArgument, 
                                                                                      typeArgumentWithVariances);
        
        com.redhat.ceylon.model.typechecker.model.Type superType = decl.getExtendedType();
        if(superType != null){
            com.redhat.ceylon.model.typechecker.model.Type superTypeResolved = superType.substitute(producedType);
            this.superclass = (ceylon.language.meta.declaration.OpenClassType) Metamodel.getMetamodel(superTypeResolved);
        }
        
        List<com.redhat.ceylon.model.typechecker.model.Type> satisfiedTypes = decl.getSatisfiedTypes();
        ceylon.language.meta.declaration.OpenInterfaceType[] interfaces 
            = new ceylon.language.meta.declaration.OpenInterfaceType[satisfiedTypes.size()];
        int i=0;
        for(com.redhat.ceylon.model.typechecker.model.Type pt : satisfiedTypes){
            com.redhat.ceylon.model.typechecker.model.Type resolvedPt = pt.substitute(producedType);
            interfaces[i++] = (ceylon.language.meta.declaration.OpenInterfaceType) 
                    Metamodel.getMetamodel(resolvedPt);
        }
        this.interfaces = Util.sequentialWrapper(ceylon.language.meta.declaration.OpenInterfaceType.$TypeDescriptor$, interfaces);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,ceylon.language.meta.declaration::OpenType>")
    public Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends OpenType> getTypeArguments() {
        checkInit();
        return typeArguments;
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,[ceylon.language.meta.declaration::OpenType,ceylon.language.meta.declaration::Variance]>")
    public Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends Sequence<? extends Object>> getTypeArgumentWithVariances() {
        checkInit();
        return typeArgumentWithVariances;
    }

    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.declaration.OpenType> getTypeArgumentList() {
        return Metamodel.getTypeArgumentList(this);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<[ceylon.language.meta.declaration::OpenType,ceylon.language.meta.declaration::Variance]>")
    public ceylon.language.Sequential<? extends ceylon.language.Sequence<? extends Object>> getTypeArgumentWithVarianceList() {
        return Metamodel.getTypeArgumentWithVarianceList(this);
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
        result = 37 * result + getTypeArgumentWithVariances().hashCode();
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
        return getTypeArgumentWithVariances().equals(other.getTypeArgumentWithVariances());
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(getDeclaration(), getTypeArgumentWithVariances());
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
