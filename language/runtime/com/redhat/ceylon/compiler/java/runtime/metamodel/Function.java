package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.Function$impl;
import ceylon.language.metamodel.Parameterised$impl;
import ceylon.language.metamodel.ProducedType;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class Function 
    extends Declaration
    implements ceylon.language.metamodel.Function {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(Function.class);
    
    private Sequential<? extends ceylon.language.metamodel.TypeParameter> typeParameters;
    
    private ProducedType type;

    public Function(com.redhat.ceylon.compiler.typechecker.model.Method declaration) {
        super(declaration);

        // FIXME: share with ClassOrInterface
        List<com.redhat.ceylon.compiler.typechecker.model.TypeParameter> typeParameters = declaration.getTypeParameters();
        ceylon.language.metamodel.TypeParameter[] typeParametersArray = new ceylon.language.metamodel.TypeParameter[typeParameters.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : typeParameters){
            typeParametersArray[i++] = new com.redhat.ceylon.compiler.java.runtime.metamodel.TypeParameter(tp);
        }
        this.typeParameters = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.TypeParameter.$TypeDescriptor, typeParametersArray);
        
        this.type = Metamodel.getMetamodel(declaration.getType());
    }

    @Override
    @Ignore
    public Function$impl $ceylon$language$metamodel$Function$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Parameterised$impl $ceylon$language$metamodel$Parameterised$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::TypeParameter>")
    public Sequential<? extends ceylon.language.metamodel.TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::TypeParameter|ceylon.language::Null")
    public ceylon.language.metamodel.TypeParameter getTypeParameter(@Name("name") String name) {
        Iterator<? extends ceylon.language.metamodel.TypeParameter> iterator = typeParameters.iterator();
        Object it;
        while((it = iterator.next()) != finished_.getFinished$()){
            ceylon.language.metamodel.TypeParameter tp = (ceylon.language.metamodel.TypeParameter) it;
            if(tp.getName().equals(name))
                return tp;
        }
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::ProducedType")
    public ProducedType getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
