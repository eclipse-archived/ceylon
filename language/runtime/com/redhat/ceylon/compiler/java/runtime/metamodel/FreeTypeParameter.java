package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.model.declaration.AnnotatedDeclaration;
import ceylon.language.model.declaration.Declaration$impl;
import ceylon.language.model.declaration.OpenType;
import ceylon.language.model.declaration.TypeParameter$impl;

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
    implements ceylon.language.model.declaration.TypeParameter, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeTypeParameter.class);
    
    TypeParameter declaration;

    private volatile boolean initialised = false;

    private OpenType defaultValue;

    private Sequential<? extends OpenType> enumeratedBounds;

    private Sequential<? extends OpenType> upperBounds;

    private FreeTopLevelOrMemberDeclaration container;
    
    @Override
    public String toString() {
        String string = declaration.getName();
        return string;
    }
    
    public FreeTypeParameter(com.redhat.ceylon.compiler.typechecker.model.TypeParameter declaration) {
        this.declaration = declaration;
    }

    protected final void checkInit(){
        if(!initialised ){
            // FIXME: lock on model loader?
            synchronized(this){
                if(!initialised){
                    init();
                    initialised = true;
                }
            }
        }
    }

    private void init() {
        if(declaration.isDefaulted())
            defaultValue = Metamodel.getMetamodel(declaration.getDefaultTypeArgument());
        else
            defaultValue = null;
        if(declaration.getCaseTypes() != null)
            enumeratedBounds = Metamodel.getMetamodelSequential(declaration.getCaseTypes());
        else
            enumeratedBounds = (Sequential)empty_.$get();
        upperBounds = Metamodel.getMetamodelSequential(declaration.getSatisfiedTypes());
        container = Metamodel.getOrCreateMetamodel(declaration.getDeclaration());
    }

    @Override
    @Ignore
    public Declaration$impl $ceylon$language$model$declaration$Declaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public TypeParameter$impl $ceylon$language$model$declaration$TypeParameter$impl() {
        // TODO Auto-generated method stub
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
        ceylon.language.model.declaration.AnnotatedDeclaration container = Metamodel.getContainer(declaration);
        return container.getQualifiedName() + "." + getName();
    }
    
    @Override
    public boolean getDefaulted(){
        return declaration.isDefaulted();
    }

    @Override
    public boolean getInvariant(){
        return declaration.isInvariant();
    }

    @Override
    public boolean getCovariant(){
        return declaration.isCovariant();
    }

    @Override
    public boolean getContravariant(){
        return declaration.isContravariant();
    }

    @TypeInfo("ceylon.language::Null|ceylon.language.model.declaration::OpenType")
    @Override
    public ceylon.language.model.declaration.OpenType getDefaultValue(){
        checkInit();
        return defaultValue;
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language.model.declaration::OpenType>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.model.declaration.OpenType> getUpperBounds(){
        checkInit();
        return upperBounds;
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language.model.declaration::OpenType>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.model.declaration.OpenType> getEnumeratedBounds(){
        checkInit();
        return enumeratedBounds;
    }

    @Override
    public ceylon.language.model.declaration.TopLevelOrMemberDeclaration getContainer(){
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
        if(obj instanceof ceylon.language.model.declaration.Package == false)
            return false;
        ceylon.language.model.declaration.Package other = (ceylon.language.model.declaration.Package) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }
}
