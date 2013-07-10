package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.metamodel.Member$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Value;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class FreeVariableWithMember<Type, Declaration extends ceylon.language.metamodel.DeclarationType> 
    extends FreeVariable 
    implements ceylon.language.metamodel.Member<Type, Declaration> {

    private AppliedMember<Type, Declaration> memberDelegate;
    @Ignore
    private TypeDescriptor $reifiedType;
    @Ignore
    private TypeDescriptor $reifiedDeclaration;

    protected FreeVariableWithMember(@Ignore TypeDescriptor $reifiedType,
            @Ignore TypeDescriptor $reifiedDeclaration,
            Value declaration) {
        super(declaration);
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        final FreeAttribute attributeDecl = FreeAttribute.instance(modelDecl);
        
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface container = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) declaration.getContainer();
        final ProducedType appliedType = declaration.getProducedReference(container.getType(), producedTypes).getType();
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(container.getType());
        TypeDescriptor attributeType = Metamodel.getTypeDescriptorForProducedType(appliedType);
        this.$reifiedDeclaration = TypeDescriptor.klass(ceylon.language.metamodel.Attribute.class, attributeType);
        memberDelegate = new AppliedMember<Type, Declaration>($reifiedType, $reifiedDeclaration){
            @Override
            protected Declaration bindTo(Object instance) {
                return (Declaration) new AppliedVariable(null, attributeDecl, appliedType, instance);
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
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(FreeVariableWithMember.class, $reifiedType, $reifiedDeclaration);
    }
}
