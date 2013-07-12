package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.ClassOrInterface$impl;
import ceylon.language.metamodel.DeclarationType$impl;
import ceylon.language.metamodel.Function;
import ceylon.language.metamodel.InterfaceType$impl;
import ceylon.language.metamodel.Member;
import ceylon.language.metamodel.Member$impl;
import ceylon.language.metamodel.MemberInterface;
import ceylon.language.metamodel.MemberInterface$impl;
import ceylon.language.metamodel.Type$impl;
import ceylon.language.metamodel.Value;
import ceylon.language.metamodel.declaration.InterfaceDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Interface;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class FreeInterfaceWithMember<Container, Type> 
    extends FreeInterface
    implements ceylon.language.metamodel.MemberInterface<Container, Type> {

    private MemberInterface<Container, Type> memberDelegate;
    @Ignore
    private TypeDescriptor $reifiedContainer;
    @Ignore
    private TypeDescriptor $reifiedType;

    public FreeInterfaceWithMember(@Ignore TypeDescriptor $reifiedContainer,
            @Ignore TypeDescriptor $reifiedType,
            Interface declaration) {
        super(declaration);
        this.$reifiedContainer = $reifiedContainer;
        this.$reifiedType = $reifiedType;
    }

    @Override
    @Ignore
    public Member$impl<Container, ceylon.language.metamodel.Interface<? extends Type>> $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public InterfaceType$impl<Type> $ceylon$language$metamodel$InterfaceType$impl() {
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
    public Type$impl $ceylon$language$metamodel$Type$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public MemberInterface$impl<Container, Type> $ceylon$language$metamodel$MemberInterface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public ClassOrInterface$impl<Type> $ceylon$language$metamodel$ClassOrInterface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void init() {
        super.init();
        memberDelegate = new AppliedMemberInterface($reifiedContainer, $reifiedType, ((com.redhat.ceylon.compiler.typechecker.model.Class)declaration).getType());
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Interface<? extends Type> $call() {
        checkInit();
        return memberDelegate.$call();
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Interface<? extends Type> $call(Object arg0) {
        checkInit();
        return memberDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Interface<? extends Type> $call(Object arg0, Object arg1) {
        checkInit();
        return memberDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Interface<? extends Type> $call(Object arg0, Object arg1, Object arg2) {
        checkInit();
        return memberDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public ceylon.language.metamodel.Interface<? extends Type> $call(Object... args) {
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
    @TypeInfo("ceylon.language.metamodel.declaration::ClassDeclaration")
    public InterfaceDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Member<SubType,Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "SubType"), 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Value<ceylon.language::Anything>") 
    })
    public <SubType, Kind extends Value<? extends Object>> Member<? super SubType, ? extends Kind> getAttribute(@Ignore TypeDescriptor arg0, @Ignore TypeDescriptor arg1, @Name("name") @TypeInfo("ceylon.language::String") String arg2) {
        return memberDelegate.getAttribute(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Member<? super SubType, ? extends Kind> getClassOrInterface(TypeDescriptor arg0, TypeDescriptor arg1, String arg2) {
        return memberDelegate.getClassOrInterface(arg0, arg1, arg2);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Member<SubType,Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "SubType"), 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::ClassOrInterface<ceylon.language::Anything>")
    })
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Member<? super SubType, ? extends Kind> getClassOrInterface(@Ignore TypeDescriptor arg0, @Ignore TypeDescriptor arg1, @Name("name") @TypeInfo("ceylon.language::String") String arg2, @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") Sequential<? extends ceylon.language.metamodel.Type> arg3) {
        return memberDelegate.getClassOrInterface(arg0, arg1, arg2, arg3);
    }

    @Override
    @Ignore
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Sequential<? extends ceylon.language.metamodel.Type> getClassOrInterface$types(TypeDescriptor arg0, TypeDescriptor arg1, String arg2) {
        return memberDelegate.getClassOrInterface$types(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public <SubType, Kind extends Function> Member<? super SubType, ? extends Kind> getFunction(TypeDescriptor arg0, TypeDescriptor arg1, String arg2) {
        return memberDelegate.getFunction(arg0, arg1, arg2);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Member<SubType,Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Function<ceylon.language::Anything,ceylon.language::Nothing>")
    })
    public <SubType, Kind extends Function> Member<? super SubType, ? extends Kind> getFunction(@Ignore TypeDescriptor arg0, @Ignore TypeDescriptor arg1, @Name("name") @TypeInfo("ceylon.language::String") String arg2, @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") Sequential<? extends ceylon.language.metamodel.Type> arg3) {
        return memberDelegate.getFunction(arg0, arg1, arg2, arg3);
    }

    @Override
    @Ignore
    public <SubType, Kind extends Function> Sequential<? extends ceylon.language.metamodel.Type> getFunction$types(TypeDescriptor arg0, TypeDescriptor arg1, String arg2) {
        return memberDelegate.getFunction$types(arg0, arg1, arg2);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Interface<ceylon.language::Anything>>")
    public Sequential<? extends ceylon.language.metamodel.Interface<? extends Object>> getInterfaces() {
        return memberDelegate.getInterfaces();
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Class<ceylon.language::Anything,ceylon.language::Nothing>")
    public ceylon.language.metamodel.Class getSuperclass() {
        return memberDelegate.getSuperclass();
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.metamodel.declaration::TypeParameter,ceylon.language.metamodel::Type>")
    public Map<? extends ceylon.language.metamodel.declaration.TypeParameter, ? extends ceylon.language.metamodel.Type> getTypeArguments() {
        return memberDelegate.getTypeArguments();
    }

    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(FreeInterfaceWithMember.class, $reifiedContainer, $reifiedType);
    }
}
