package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.AnnotatedDeclaration;
import ceylon.language.meta.declaration.Declaration$impl;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.TypeParameter$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeTypeParameter
    implements ceylon.language.meta.declaration.TypeParameter, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeTypeParameter.class);
    
    TypeParameter declaration;

    private volatile boolean initialised = false;

    private OpenType defaultTypeArgument;

    private Sequential<? extends OpenType> caseTypes;

    private Sequential<? extends OpenType> satisfiedTypes;

    private FreeNestableDeclaration container;
    
    public FreeTypeParameter(com.redhat.ceylon.compiler.typechecker.model.TypeParameter declaration) {
        this.declaration = declaration;
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
        if(declaration.isDefaulted())
            defaultTypeArgument = Metamodel.getMetamodel(declaration.getDefaultTypeArgument());
        else
            defaultTypeArgument = null;
        if(declaration.getCaseTypes() != null)
            caseTypes = Metamodel.getMetamodelSequential(declaration.getCaseTypes());
        else
            caseTypes = (Sequential)empty_.get_();
        satisfiedTypes = Metamodel.getMetamodelSequential(declaration.getSatisfiedTypes());
        container = Metamodel.getOrCreateMetamodel(declaration.getDeclaration());
    }

    @Override
    @Ignore
    public Declaration$impl $ceylon$language$meta$declaration$Declaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public TypeParameter$impl $ceylon$language$meta$declaration$TypeParameter$impl() {
        return null;
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

    @Override
    public String getName() {
        return declaration.getName();
    }

    @Override
    public String getQualifiedName() {
        ceylon.language.meta.declaration.AnnotatedDeclaration container = Metamodel.getContainer(declaration);
        return container.getQualifiedName() + "." + getName();
    }
    
    @Override
    public boolean getDefaulted(){
        return declaration.isDefaulted();
    }

    @Override
    public ceylon.language.meta.declaration.Variance getVariance(){
        if(declaration.isInvariant())
            return ceylon.language.meta.declaration.invariant_.get_();
        if(declaration.isCovariant())
            return ceylon.language.meta.declaration.covariant_.get_();
        if(declaration.isContravariant())
            return ceylon.language.meta.declaration.contravariant_.get_();
        throw new RuntimeException("Underlying declaration is neither invariant, covariant nor contravariant");
    }

    @TypeInfo("ceylon.language::Null|ceylon.language.meta.declaration::OpenType")
    @Override
    public ceylon.language.meta.declaration.OpenType getDefaultTypeArgument(){
        checkInit();
        return defaultTypeArgument;
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::OpenType>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.declaration.OpenType> getSatisfiedTypes(){
        checkInit();
        return satisfiedTypes;
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::OpenType>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.declaration.OpenType> getCaseTypes(){
        checkInit();
        return caseTypes;
    }

    @Override
    public ceylon.language.meta.declaration.NestableDeclaration getContainer(){
        checkInit();
        return container;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        AnnotatedDeclaration container = getContainer();
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
        if(obj instanceof ceylon.language.meta.declaration.Package == false)
            return false;
        ceylon.language.meta.declaration.Package other = (ceylon.language.meta.declaration.Package) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }

    @Override
    public String toString() {
        return "given " + getContainer().getQualifiedName() + "." + getName();
    }
}
