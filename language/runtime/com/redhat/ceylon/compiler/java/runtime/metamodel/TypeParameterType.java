package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.ProducedType$impl;
import ceylon.language.metamodel.TypeParameterType$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class TypeParameterType 
    implements ceylon.language.metamodel.TypeParameterType, ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(TypeParameterType.class);
    
    protected ceylon.language.metamodel.TypeParameter declaration;

    private TypeParameter wrapped;

    private volatile boolean initialised;
    
    TypeParameterType(TypeParameter tp){
        this.wrapped = tp;
    }

    @Override
    @Ignore
    public ProducedType$impl $ceylon$language$metamodel$ProducedType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public TypeParameterType$impl $ceylon$language$metamodel$TypeParameterType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::TypeParameter")
    public ceylon.language.metamodel.TypeParameter getDeclaration() {
        checkInit();
        return declaration;
    }

    private void init(){
        // we need to find where it came from to look up the proper wrapper
        Scope container = wrapped.getContainer();
        // FIXME: support more container sources, such as methods and outer declarations
        if(container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface){
            com.redhat.ceylon.compiler.java.runtime.metamodel.ClassOrInterface containerMetamodel = Metamodel.getOrCreateMetamodel((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) container);
            ceylon.language.metamodel.TypeParameter typeParameter = containerMetamodel.getTypeParameter(wrapped.getName());
            if(typeParameter != null)
                this.declaration = typeParameter;
            else
                throw new RuntimeException("Failed to find type parameter: "+wrapped.getName()+" in container "+container);
        }else if(container instanceof com.redhat.ceylon.compiler.typechecker.model.Method){
            // try to find it in the method
            com.redhat.ceylon.compiler.java.runtime.metamodel.Function method = Metamodel.getMetamodel((com.redhat.ceylon.compiler.typechecker.model.Method)container);
            ceylon.language.metamodel.TypeParameter typeParameter = method.getTypeParameter(wrapped.getName());
            if(typeParameter != null)
                this.declaration = typeParameter;
            else
                throw new RuntimeException("Failed to find type parameter: "+wrapped.getName()+" in container "+container);
        }else
            throw new RuntimeException("Declaration container type not supported yet: "+container);
    }
    
    private void checkInit() {
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

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
