package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.model.Attribute$impl;
import ceylon.language.model.AttributeModel$impl;
import ceylon.language.model.ClassOrInterface;
import ceylon.language.model.Member$impl;
import ceylon.language.model.Model$impl;
import ceylon.language.model.VariableAttribute$impl;
import ceylon.language.model.declaration.ValueDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.NONE),
})
public class FreeVariableWithAppliedVariableAttribute<Container, Type> 
    extends FreeVariable 
    implements ceylon.language.model.VariableAttribute<Container, Type> {

    private ceylon.language.model.VariableAttribute<Container, Type> memberDelegate;
    @Ignore
    private TypeDescriptor $reifiedContainer;
    @Ignore
    private TypeDescriptor $reifiedType;
    private ceylon.language.model.Type closedType;

    protected FreeVariableWithAppliedVariableAttribute(@Ignore TypeDescriptor $reifiedContainer,
            @Ignore TypeDescriptor $reifiedType,
            com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);

        // FIXME: same code in FreeAttributeWithMember
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface container = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) declaration.getContainer();
        ceylon.language.model.ClassOrInterface<? extends Object> appliedContainer = (ClassOrInterface<? extends Object>) Metamodel.getAppliedMetamodel(container.getType());
        ProducedTypedReference typedReference = declaration.getProducedTypedReference(container.getType(), producedTypes);
        ProducedType appliedType = typedReference.getType();
        this.$reifiedContainer = Metamodel.getTypeDescriptorForProducedType(container.getType());
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(appliedType);
        memberDelegate = new AppliedVariableAttribute<Container, Type>(this.$reifiedContainer, this.$reifiedType, this, typedReference, appliedContainer);
        
        this.closedType = Metamodel.getAppliedMetamodel(appliedType);
    }

    @Override
    @Ignore
    public Member$impl $ceylon$language$model$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AttributeModel$impl<Type> $ceylon$language$model$AttributeModel$impl() {
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
    public Attribute$impl<Container, Type> $ceylon$language$model$Attribute$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public VariableAttribute$impl<Container, Type> $ceylon$language$model$VariableAttribute$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::ValueDeclaration")
    public ValueDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.model::ClassOrInterface<ceylon.language::Anything>")
    public ceylon.language.model.ClassOrInterface<? extends Object> getDeclaringClassOrInterface() {
        return memberDelegate.getDeclaringClassOrInterface();
    }

    @Override
    @TypeInfo("ceylon.language.model::Type")
    public ceylon.language.model.Type getType() {
        return closedType;
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call() {
        return (ceylon.language.model.Variable<Type>)memberDelegate.$call();
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call(Object arg0) {
        return (ceylon.language.model.Variable<Type>)memberDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call(Object arg0, Object arg1) {
        return (ceylon.language.model.Variable<Type>)memberDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call(Object arg0, Object arg1, Object arg2) {
        return (ceylon.language.model.Variable<Type>)memberDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call(Object... args) {
        return (ceylon.language.model.Variable<Type>)memberDelegate.$call(args);
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        return memberDelegate.$getVariadicParameterIndex();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(FreeAttributeWithAppliedAttribute.class, $reifiedContainer, $reifiedType);
    }
}
