package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.Function;
import ceylon.language.metamodel.Member;
import ceylon.language.metamodel.Member$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class FreeFunctionWithMember<Type, Declaration extends ceylon.language.metamodel.DeclarationType> 
    extends FreeFunction 
    implements ceylon.language.metamodel.Member<Type, Declaration> {

    private AppliedMember<Type, Declaration> memberDelegate;
    @Ignore
    private TypeDescriptor $reifiedType;
    @Ignore
    private TypeDescriptor $reifiedDeclaration;

    public FreeFunctionWithMember(@Ignore TypeDescriptor $reifiedType, 
            @Ignore TypeDescriptor $reifiedDeclaration,
            Method declaration) {
        super(declaration);
        
        final FreeFunction method = new FreeFunction(declaration);
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();

        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface container = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) declaration.getContainer();
        final ProducedReference appliedFunction = declaration.getProducedReference(container.getType(), producedTypes);
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(container.getType());
        // FIXME: check with MPL
        TypeDescriptor returnType = Metamodel.getTypeDescriptorForProducedType(appliedFunction.getType());
        TypeDescriptor reifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.getUnit(), (Functional) declaration, appliedFunction);
        this.$reifiedDeclaration = TypeDescriptor.klass(ceylon.language.metamodel.Function.class, returnType, reifiedArguments);
        memberDelegate = new AppliedMember<Type, Declaration>($reifiedType, $reifiedDeclaration){
            @Override
            protected Declaration bindTo(Object instance) {
                return (Declaration) new AppliedFunction(null, null, appliedFunction, method, instance);
            }
        };
    }

    @Override
    @Ignore
    public Member$impl<Type, Declaration> $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Declaration $call() {
        return memberDelegate.$call();
    }

    @Override
    @Ignore
    public Declaration $call(Object arg0) {
        return memberDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public Declaration $call(Object arg0, Object arg1) {
        return memberDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public Declaration $call(Object arg0, Object arg1, Object arg2) {
        return memberDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public Declaration $call(Object... args) {
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
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(FreeFunctionWithMember.class, $reifiedType, $reifiedDeclaration);
    }
}
