package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.metamodel.Attribute$impl;
import ceylon.language.metamodel.AttributeModel$impl;
import ceylon.language.metamodel.Model$impl;
import ceylon.language.metamodel.Member$impl;
import ceylon.language.metamodel.VariableAttribute$impl;
import ceylon.language.metamodel.declaration.AttributeDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Value;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.NONE),
})
public class FreeVariableWithAppliedVariableAttribute<Container, Type> 
    extends FreeVariable 
    implements ceylon.language.metamodel.VariableAttribute<Container, Type> {

    private ceylon.language.metamodel.VariableAttribute<Container, Type> memberDelegate;
    @Ignore
    private TypeDescriptor $reifiedContainer;
    @Ignore
    private TypeDescriptor $reifiedType;
    private ceylon.language.metamodel.Type closedType;

    protected FreeVariableWithAppliedVariableAttribute(@Ignore TypeDescriptor $reifiedContainer,
            @Ignore TypeDescriptor $reifiedType,
            Value declaration) {
        super(declaration);
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        final FreeAttribute attributeDecl = FreeAttribute.instance(modelDecl);

        // FIXME: same code in FreeAttributeWithMember
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface container = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) declaration.getContainer();
        final ProducedType appliedType = declaration.getProducedReference(container.getType(), producedTypes).getType();
        this.$reifiedContainer = Metamodel.getTypeDescriptorForProducedType(container.getType());
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(appliedType);
        memberDelegate = new AppliedVariableAttribute<Container, Type>(this.$reifiedContainer, this.$reifiedType, attributeDecl, appliedType);
        
        this.closedType = Metamodel.getAppliedMetamodel(appliedType);
    }

    @Override
    @Ignore
    public Member$impl $ceylon$language$metamodel$Member$impl() {
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
    @Ignore
    public VariableAttribute$impl<Container, Type> $ceylon$language$metamodel$VariableAttribute$impl() {
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
    public ceylon.language.metamodel.Variable<Type> $call() {
        return (ceylon.language.metamodel.Variable<Type>)memberDelegate.$call();
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Variable<Type> $call(Object arg0) {
        return (ceylon.language.metamodel.Variable<Type>)memberDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Variable<Type> $call(Object arg0, Object arg1) {
        return (ceylon.language.metamodel.Variable<Type>)memberDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Variable<Type> $call(Object arg0, Object arg1, Object arg2) {
        return (ceylon.language.metamodel.Variable<Type>)memberDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Variable<Type> $call(Object... args) {
        return (ceylon.language.metamodel.Variable<Type>)memberDelegate.$call(args);
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        return memberDelegate.$getVariadicParameterIndex();
    }
    
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(FreeAttributeWithAppliedAttribute.class, $reifiedContainer, $reifiedType);
    }
}
