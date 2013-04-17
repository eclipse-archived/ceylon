package com.redhat.ceylon.compiler.java.runtime.metamodel;

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

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })
public class ClassType<Type, Arguments extends Sequential<? extends Object>> 
    extends ClassOrInterfaceType<Type>
    implements ceylon.language.metamodel.ClassType<Type, Arguments> {

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
    public TypeDescriptor $getType() {
        checkInit();
        com.redhat.ceylon.compiler.java.runtime.metamodel.Class<Type, Arguments> declaration = (com.redhat.ceylon.compiler.java.runtime.metamodel.Class<Type, Arguments>) this.declaration;
        return TypeDescriptor.klass(ClassType.class, declaration.$getReifiedType(), declaration.$getReifiedArguments());
    }
}
