package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.meta.declaration.OpenType$impl;
import ceylon.language.meta.declaration.OpenTypeVariable$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;

@Ceylon(major = 6)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeTypeParameterType 
    implements ceylon.language.meta.declaration.OpenTypeVariable, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeTypeParameterType.class);
    
    protected ceylon.language.meta.declaration.TypeParameter declaration;

    private TypeParameter wrapped;

    private volatile boolean initialised;
    
    FreeTypeParameterType(TypeParameter tp){
        this.wrapped = tp;
    }

    @Override
    @Ignore
    public OpenType$impl $ceylon$language$meta$declaration$OpenType$impl() {
        return null;
    }

    @Override
    @Ignore
    public OpenTypeVariable$impl $ceylon$language$meta$declaration$OpenTypeVariable$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::TypeParameter")
    public ceylon.language.meta.declaration.TypeParameter getDeclaration() {
        checkInit();
        return declaration;
    }

    private void init(){
        // we need to find where it came from to look up the proper wrapper
        Scope container = wrapped.getContainer();
        if(container instanceof com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration){
            ceylon.language.meta.declaration.GenericDeclaration containerMetamodel = 
                    (ceylon.language.meta.declaration.GenericDeclaration) 
                    Metamodel.getOrCreateMetamodel((com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration) container);
            ceylon.language.meta.declaration.TypeParameter typeParameter = containerMetamodel.getTypeParameterDeclaration(wrapped.getName());
            if(typeParameter != null)
                this.declaration = typeParameter;
            else
                throw new RuntimeException("Failed to find type parameter: "+wrapped.getName()+" in container "+container);
        }else if(container instanceof com.redhat.ceylon.compiler.typechecker.model.Method){
            // try to find it in the method
            ceylon.language.meta.declaration.FunctionDeclaration method = Metamodel.getMetamodel((com.redhat.ceylon.compiler.typechecker.model.Method)container);
            ceylon.language.meta.declaration.TypeParameter typeParameter = method.getTypeParameterDeclaration(wrapped.getName());
            if(typeParameter != null)
                this.declaration = typeParameter;
            else
                throw new RuntimeException("Failed to find type parameter: "+wrapped.getName()+" in container "+container);
        }else
            throw new RuntimeException("Declaration container type not supported yet: "+container);
    }
    
    private void checkInit() {
        if(!initialised){
            synchronized(Metamodel.getLock()){
                if(!initialised){
                    init();
                    initialised = true;
                }
            }
        }
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaration().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.declaration.OpenTypeVariable == false)
            return false;
        ceylon.language.meta.declaration.OpenTypeVariable other = (ceylon.language.meta.declaration.OpenTypeVariable) obj;
        return getDeclaration().equals(other.getDeclaration());
    }

    @Override
    public String toString() {
        return getDeclaration().toString();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
