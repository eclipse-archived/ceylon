package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.metamodel.Attribute$impl;
import ceylon.language.metamodel.AttributeType$impl;
import ceylon.language.metamodel.DeclarationType$impl;
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
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class FreeAttributeWithMember<Container, Type> 
    extends FreeAttribute 
    implements ceylon.language.metamodel.Attribute<Container, Type> {

    @Ignore
    private TypeDescriptor $reifiedContainer;
    @Ignore
    private TypeDescriptor $reifiedType;

    private ceylon.language.metamodel.Type closedType;
    private AppliedAttribute<Container, Type> memberDelegate;

    protected FreeAttributeWithMember(@Ignore TypeDescriptor $reifiedContainer,
            @Ignore TypeDescriptor $reifiedType,
            TypedDeclaration declaration) {
        super(declaration);
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        final FreeAttribute attributeDecl = FreeAttribute.instance(modelDecl);
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface container = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) declaration.getContainer();
        final ProducedType appliedType = declaration.getProducedReference(container.getType(), producedTypes).getType();
        this.$reifiedContainer = Metamodel.getTypeDescriptorForProducedType(container.getType());
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(appliedType);
        memberDelegate = new AppliedAttribute<Container, Type>(this.$reifiedContainer, this.$reifiedType, attributeDecl, appliedType);
        
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
    public AttributeType$impl<Type> $ceylon$language$metamodel$AttributeType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public DeclarationType$impl $ceylon$language$metamodel$DeclarationType$impl() {
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
    
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(FreeAttributeWithMember.class, $reifiedContainer, $reifiedType);
    }
}
