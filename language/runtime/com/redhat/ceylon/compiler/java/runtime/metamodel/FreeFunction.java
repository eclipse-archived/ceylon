package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.metamodel.untyped.Function$impl;
import ceylon.language.metamodel.untyped.Declaration$impl;
import ceylon.language.metamodel.untyped.Parameterised$impl;
import ceylon.language.metamodel.untyped.Type;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeFunction 
    extends FreeDeclaration
    implements ceylon.language.metamodel.untyped.Function {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeFunction.class);
    
    private Sequential<? extends ceylon.language.metamodel.untyped.TypeParameter> typeParameters;
    
    private Type type;

    public FreeFunction(com.redhat.ceylon.compiler.typechecker.model.Method declaration) {
        super(declaration);

        // FIXME: share with ClassOrInterface
        List<com.redhat.ceylon.compiler.typechecker.model.TypeParameter> typeParameters = declaration.getTypeParameters();
        ceylon.language.metamodel.untyped.TypeParameter[] typeParametersArray = new ceylon.language.metamodel.untyped.TypeParameter[typeParameters.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : typeParameters){
            typeParametersArray[i++] = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter(tp);
        }
        this.typeParameters = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.untyped.TypeParameter.$TypeDescriptor, typeParametersArray);
        
        this.type = Metamodel.getMetamodel(declaration.getType());
    }

    @Override
    @Ignore
    public Function$impl $ceylon$language$metamodel$untyped$Function$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Declaration$impl $ceylon$language$metamodel$untyped$Declaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Parameterised$impl $ceylon$language$metamodel$untyped$Parameterised$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.untyped::TypeParameter>")
    public Sequential<? extends ceylon.language.metamodel.untyped.TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.untyped::TypeParameter|ceylon.language::Null")
    public ceylon.language.metamodel.untyped.TypeParameter getTypeParameter(@Name("name") String name) {
        Iterator<? extends ceylon.language.metamodel.untyped.TypeParameter> iterator = typeParameters.iterator();
        Object it;
        while((it = iterator.next()) != finished_.getFinished$()){
            ceylon.language.metamodel.untyped.TypeParameter tp = (ceylon.language.metamodel.untyped.TypeParameter) it;
            if(tp.getName().equals(name))
                return tp;
        }
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.untyped::Type")
    public Type getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
