package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.Member;
import ceylon.language.metamodel.Member$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type"),
    @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::DeclarationType")
})
public class FreeInterfaceWithMember<Type, Declaration extends ceylon.language.metamodel.DeclarationType> 
    extends FreeInterface
    implements ceylon.language.metamodel.Member<Type, Declaration> {

    private AppliedMember<Type, Declaration> memberDelegate;
    @Ignore
    private TypeDescriptor $reifiedType;
    @Ignore
    private TypeDescriptor $reifiedDeclaration;

    public FreeInterfaceWithMember(@Ignore TypeDescriptor $reifiedType,
            @Ignore TypeDescriptor $reifiedDeclaration,
            Interface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public Member$impl<Type, Declaration> $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void init() {
        super.init();
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface container = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) declaration.getContainer();
        final ProducedType appliedType = declaration.getProducedReference(container.getType(), producedTypes).getType();
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(container.getType());
        TypeDescriptor classType = Metamodel.getTypeDescriptorForProducedType(appliedType);
        TypeDescriptor reifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.getUnit(), (Functional) declaration, appliedType);
        this.$reifiedDeclaration = TypeDescriptor.klass(ceylon.language.metamodel.Class.class, classType, reifiedArguments);
        memberDelegate = new AppliedMember<Type, Declaration>($reifiedType, $reifiedDeclaration){
            @Override
            protected Declaration bindTo(Object instance) {
                return (Declaration) new AppliedClassType(null, null, appliedType, instance);
            }
        };
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Member<Container,Kind>")
    @TypeParameters({ 
        @TypeParameter("Container"), 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::ClassOrInterface<ceylon.language::Anything>") 
    })
    public <Container, Kind extends ClassOrInterface<? extends Object>> Member<Container, Kind> memberApply(@Ignore TypeDescriptor $reifiedContainer, @Ignore TypeDescriptor $reifiedKind, @Name("types") @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types) {
        // TODO: check arguments
        return (Member)this;
    }
    
    @Override
    @Ignore
    public Declaration $call() {
        checkInit();
        return memberDelegate.$call();
    }

    @Override
    @Ignore
    public Declaration $call(Object arg0) {
        checkInit();
        return memberDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public Declaration $call(Object arg0, Object arg1) {
        checkInit();
        return memberDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public Declaration $call(Object arg0, Object arg1, Object arg2) {
        checkInit();
        return memberDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public Declaration $call(Object... args) {
        checkInit();
        return memberDelegate.$call(args);
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        checkInit();
        return memberDelegate.$getVariadicParameterIndex();
    }

//    @Override
//    @TypeInfo("ceylon.language.metamodel::ClassOrInterface<Type>")
//    public ClassOrInterface<? extends Type> getDeclaringClassOrInterface() {
//        // TODO Auto-generated method stub
//        return null;
//    }

    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(FreeInterfaceWithMember.class, $reifiedType, $reifiedDeclaration);
    }
}
