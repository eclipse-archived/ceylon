package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Empty;
import ceylon.language.Sequential;
import ceylon.language.metamodel.Class$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })
public class Class<Type, Arguments extends Sequential<? extends Object>> 
    extends ClassOrInterface<Type>
    implements ceylon.language.metamodel.Class<Type, Arguments> {

    @Ignore
    private TypeDescriptor $reifiedArguments;

    public Class(com.redhat.ceylon.compiler.typechecker.model.Class declaration) {
        super(declaration);
    }

    @Override
    protected void init() {
        super.init();
        $reifiedArguments = Empty.$TypeDescriptor;
    }
    
    @Override
    @Ignore
    public Class$impl<Type, Arguments> $ceylon$language$metamodel$Class$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call(Object arg0, Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call(Object arg0, Object arg1, Object arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call(Object... args) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(Class.class, $reifiedType, $reifiedArguments);
    }

    @Ignore
    TypeDescriptor $getReifiedArguments(){
        checkInit();
        return $reifiedArguments;
    }
}
