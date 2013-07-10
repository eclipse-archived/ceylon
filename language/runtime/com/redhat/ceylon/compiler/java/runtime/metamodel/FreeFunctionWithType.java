package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.DeclarationType$impl;
import ceylon.language.metamodel.Function;
import ceylon.language.metamodel.Function$impl;
import ceylon.language.metamodel.declaration.FunctionDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Method;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })

public class FreeFunctionWithType<Type, Arguments extends Sequential<? extends Object>>
    extends FreeFunction 
    implements ceylon.language.metamodel.Function<Type, Arguments> {

    private AppliedFunction<Type,Arguments> typeDelegate;

    public FreeFunctionWithType(@Ignore TypeDescriptor $reifiedType, 
            @Ignore TypeDescriptor $reifiedArguments,
            Method declaration) {
        super(declaration);
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        com.redhat.ceylon.compiler.typechecker.model.ProducedReference appliedFunction = declaration.getProducedReference(null, producedTypes);
        typeDelegate = new AppliedFunction(null, null, appliedFunction, this, null);
    }

    @Override
    @Ignore
    public DeclarationType$impl $ceylon$language$metamodel$DeclarationType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Function$impl<Type, Arguments> $ceylon$language$metamodel$Function$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Type $call() {
        return typeDelegate.$call();
    }

    @Override
    @Ignore
    public Type $call(Object arg0) {
        return typeDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public Type $call(Object arg0, Object arg1) {
        return typeDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public Type $call(Object arg0, Object arg1, Object arg2) {
        return typeDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public Type $call(Object... args) {
        return typeDelegate.$call(args);
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        return typeDelegate.$getVariadicParameterIndex();
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Type")
    public ceylon.language.metamodel.Type getType() {
        return typeDelegate.getType();
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::FunctionDeclaration")
    public FunctionDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Function<ceylon.language::Anything,ceylon.language::Nothing>")
    public ceylon.language.metamodel.Function<? extends Object, ? super Sequential<? extends Object>> apply(@Name("types") @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types){
        return (Function)this;
    }
    
    @Override
    public TypeDescriptor $getType() {
        TypeDescriptor.Class type = (TypeDescriptor.Class) typeDelegate.$getType();
        TypeDescriptor[] args = type.getTypeArguments();
        return TypeDescriptor.klass(FreeFunctionWithType.class, args[0], args[1]);
    }
}
