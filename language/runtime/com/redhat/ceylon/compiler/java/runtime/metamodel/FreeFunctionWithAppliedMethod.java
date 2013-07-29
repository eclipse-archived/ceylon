package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.model.ClassOrInterface;
import ceylon.language.model.FunctionModel$impl;
import ceylon.language.model.Member$impl;
import ceylon.language.model.Method$impl;
import ceylon.language.model.Model$impl;
import ceylon.language.model.declaration.FunctionDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
})
public class FreeFunctionWithAppliedMethod<Container, Type, Arguments extends Sequential<? extends Object>> 
    extends FreeFunction 
    implements ceylon.language.model.Method<Container, Type, Arguments> {

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
            com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();

        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface container = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) declaration.getContainer();
        ceylon.language.model.ClassOrInterface<? extends Object> appliedContainer = (ClassOrInterface<? extends Object>) Metamodel.getAppliedMetamodel(container.getType());
        // in theory this works because we only get instantiated if the method and containers have no TP
        final ProducedTypedReference appliedFunction = declaration.getProducedTypedReference(container.getType(), producedTypes);
        memberDelegate = new AppliedMethod($reifiedContainer, $reifiedType, $reifiedArguments, appliedFunction, this, appliedContainer);
        this.$reifiedContainer = $reifiedContainer;
        this.$reifiedType = $reifiedType;
        this.$reifiedArguments = $reifiedArguments;
    }

    @Override
    @Ignore
    public FunctionModel$impl<Type, Arguments> $ceylon$language$model$FunctionModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$model$Model$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Method$impl<Container, Type, Arguments> $ceylon$language$model$Method$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Member$impl<Container, ceylon.language.model.Function<? extends Type, ? super Arguments>> $ceylon$language$model$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public ceylon.language.model.Function<? extends Type, ? super Arguments> $call() {
        return memberDelegate.$call();
    }

    @Override
    @Ignore
    public ceylon.language.model.Function<? extends Type, ? super Arguments> $call(Object arg0) {
        return memberDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public ceylon.language.model.Function<? extends Type, ? super Arguments> $call(Object arg0, Object arg1) {
        return memberDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public ceylon.language.model.Function<? extends Type, ? super Arguments> $call(Object arg0, Object arg1, Object arg2) {
        return memberDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public ceylon.language.model.Function<? extends Type, ? super Arguments> $call(Object... args) {
        return memberDelegate.$call(args);
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        return memberDelegate.$getVariadicParameterIndex();
    }

    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.model.Method<Container, Type, Arguments> memberApply(
            @Ignore TypeDescriptor $reifiedContainer,
            @Ignore TypeDescriptor $reifiedType,
            @Ignore TypeDescriptor $reifiedArguments,
            @Name("types") @Sequenced Sequential<? extends ceylon.language.model.Type> types){
        // TODO: check arguments
        return (ceylon.language.model.Method)this;
    }
    
    @Override
    @TypeInfo("ceylon.language.model.declaration::FunctionDeclaration")
    public FunctionDeclaration getDeclaration() {
        return memberDelegate.getDeclaration();
    }

    @Override
    @TypeInfo("ceylon.language.model::ClassOrInterface<ceylon.language::Anything>")
    public ceylon.language.model.ClassOrInterface<? extends Object> getDeclaringClassOrInterface() {
        return memberDelegate.getDeclaringClassOrInterface();
    }

    @Override
    @TypeInfo("ceylon.language.model::Type")
    public ceylon.language.model.Type getType() {
        return memberDelegate.getType();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(FreeFunctionWithAppliedMethod.class, $reifiedContainer, $reifiedType, $reifiedArguments);
    }

}
