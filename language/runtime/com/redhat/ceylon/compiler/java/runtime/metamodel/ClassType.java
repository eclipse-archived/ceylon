package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.Class;
import ceylon.language.metamodel.ClassType$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })
public class ClassType<Type, Arguments extends Sequential<? extends Object>> 
    extends ClassOrInterfaceType<Type>
    implements ceylon.language.metamodel.ClassType<Type, Arguments> {

    private TypeDescriptor $reifiedArguments;
    private TypeDescriptor $reifiedType;

    public ClassType(com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType) {
        super(producedType);
    }

    @Override
    @Ignore
    public ClassType$impl<Type, Arguments> $ceylon$language$metamodel$ClassType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Class<Type,Arguments>")
    public ceylon.language.metamodel.Class<? extends Type, ? super Arguments> getDeclaration() {
        return (Class<? extends Type, ? super Arguments>) super.getDeclaration();
    }

    @Override
    protected void init() {
        super.init();
        com.redhat.ceylon.compiler.typechecker.model.Class decl = (com.redhat.ceylon.compiler.typechecker.model.Class) producedType.getDeclaration();
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> elemTypes = new LinkedList<com.redhat.ceylon.compiler.typechecker.model.ProducedType>();
        for(Parameter param : decl.getParameterList().getParameters()){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType paramType = param.getType().substitute(producedType.getTypeArguments());
            elemTypes.add(paramType);
        }
        // FIXME: last three params
        ProducedType tupleType = decl.getUnit().getTupleType(elemTypes, false, false, -1);
        this.$reifiedArguments = Metamodel.getTypeDescriptorForProducedType(tupleType);
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(producedType);
    }
    
    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(ClassType.class, $reifiedType, $reifiedArguments);
    }
}
