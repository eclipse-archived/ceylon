package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.Model$impl;
import ceylon.language.metamodel.Function;
import ceylon.language.metamodel.FunctionModel$impl;
import ceylon.language.metamodel.Member;
import ceylon.language.metamodel.Member$impl;
import ceylon.language.metamodel.Method$impl;
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
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
})
public class FreeFunctionWithAppliedMethod<Container, Type, Arguments extends Sequential<? extends Object>> 
    extends FreeFunction 
    implements ceylon.language.metamodel.Method<Container, Type, Arguments> {

    private AppliedMethod<Container, Type, Arguments> memberDelegate;
    @Ignore
    private TypeDescriptor $reifiedContainer;
    @Ignore
    private TypeDescriptor $reifiedType;
    @Ignore
    private TypeDescriptor $reifiedArguments;

    public FreeFunctionWithAppliedMethod(@Ignore TypeDescriptor $reifiedContainer, 
            @Ignore TypeDescriptor $reifiedType,
            @Ignore TypeDescriptor $reifiedArguments,
            Method declaration) {
        super(declaration);
        
        final FreeFunction method = new FreeFunction(declaration);
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();

        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface container = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) declaration.getContainer();
        // in theory this works because we only get instantiated if the method and containers have no TP
        final ProducedReference appliedFunction = declaration.getProducedReference(container.getType(), producedTypes);
        memberDelegate = new AppliedMethod($reifiedContainer, $reifiedType, $reifiedArguments, appliedFunction, method);
        this.$reifiedContainer = $reifiedContainer;
        this.$reifiedType = $reifiedType;
        this.$reifiedArguments = $reifiedArguments;
    }

    @Override
    @Ignore
    public FunctionModel$impl<Type, Arguments> $ceylon$language$metamodel$FunctionModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$metamodel$Model$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Method$impl<Container, Type, Arguments> $ceylon$language$metamodel$Method$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Member$impl<Container, ceylon.language.metamodel.Function<? extends Type, ? super Arguments>> $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Function<? extends Type, ? super Arguments> $call() {
        return memberDelegate.$call();
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Function<? extends Type, ? super Arguments> $call(Object arg0) {
        return memberDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Function<? extends Type, ? super Arguments> $call(Object arg0, Object arg1) {
        return memberDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Function<? extends Type, ? super Arguments> $call(Object arg0, Object arg1, Object arg2) {
        return memberDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Function<? extends Type, ? super Arguments> $call(Object... args) {
        return memberDelegate.$call(args);
    }


    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        return memberDelegate.$getVariadicParameterIndex();
    }

    @Override
    public <Container, Kind extends Function> Member<Container, Kind> memberApply(@Ignore TypeDescriptor $reifiedContainer, @Ignore TypeDescriptor $reifiedKind, @Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types) {
        // TODO: check arguments
        return (Member)this;
    }
    
    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::FunctionDeclaration")
    public FunctionDeclaration getDeclaration() {
        return memberDelegate.getDeclaration();
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Type")
    public ceylon.language.metamodel.Type getType() {
        return memberDelegate.getType();
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(FreeFunctionWithAppliedMethod.class, $reifiedContainer, $reifiedType, $reifiedArguments);
    }

}
