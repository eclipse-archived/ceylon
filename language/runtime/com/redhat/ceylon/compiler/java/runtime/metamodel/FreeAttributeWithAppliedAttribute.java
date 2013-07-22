package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.metamodel.Attribute$impl;
import ceylon.language.metamodel.AttributeModel$impl;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.Model$impl;
import ceylon.language.metamodel.Member$impl;
import ceylon.language.metamodel.declaration.AttributeDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class FreeAttributeWithAppliedAttribute<Container, Type> 
    extends FreeAttribute 
    implements ceylon.language.metamodel.Attribute<Container, Type> {

    @Ignore
    private TypeDescriptor $reifiedContainer;
    @Ignore
    private TypeDescriptor $reifiedType;

    private ceylon.language.metamodel.Type closedType;
    private AppliedAttribute<Container, Type> memberDelegate;

    protected FreeAttributeWithAppliedAttribute(@Ignore TypeDescriptor $reifiedContainer,
            @Ignore TypeDescriptor $reifiedType,
            TypedDeclaration declaration) {
        super(declaration);
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface container = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) declaration.getContainer();
        ceylon.language.metamodel.ClassOrInterface<? extends Object> appliedContainer = (ClassOrInterface<? extends Object>) Metamodel.getAppliedMetamodel(container.getType());
        ProducedTypedReference typedReference = declaration.getProducedTypedReference(container.getType(), producedTypes);
        ProducedType appliedType = typedReference.getType();
        this.$reifiedContainer = Metamodel.getTypeDescriptorForProducedType(container.getType());
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(appliedType);
        memberDelegate = new AppliedAttribute<Container, Type>(this.$reifiedContainer, this.$reifiedType, this, typedReference, appliedContainer);
        
        this.closedType = Metamodel.getAppliedMetamodel(appliedType);
    }

    @Override
    @Ignore
    public Member$impl<Container, ceylon.language.metamodel.Value<? extends Type>> $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public AttributeModel$impl<Type> $ceylon$language$metamodel$AttributeModel$impl() {
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
    public Attribute$impl<Container, Type> $ceylon$language$metamodel$Attribute$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::AttributeDeclaration")
    public AttributeDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::ClassOrInterface<ceylon.language::Anything>")
    public ceylon.language.metamodel.ClassOrInterface<? extends Object> getDeclaringClassOrInterface() {
        return memberDelegate.getDeclaringClassOrInterface();
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Type")
    public ceylon.language.metamodel.Type getType() {
        return closedType;
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Value<? extends Type> $call() {
        return memberDelegate.$call();
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Value<? extends Type> $call(Object arg0) {
        return memberDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Value<? extends Type> $call(Object arg0, Object arg1) {
        return memberDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Value<? extends Type> $call(Object arg0, Object arg1, Object arg2) {
        return memberDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Value<? extends Type> $call(Object... args) {
        return memberDelegate.$call(args);
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        return memberDelegate.$getVariadicParameterIndex();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(FreeAttributeWithAppliedAttribute.class, $reifiedContainer, $reifiedType);
    }
}
