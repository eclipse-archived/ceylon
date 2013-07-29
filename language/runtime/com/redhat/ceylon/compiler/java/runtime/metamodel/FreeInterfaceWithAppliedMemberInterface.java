package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.model.ClassOrInterface;
import ceylon.language.model.ClassOrInterface$impl;
import ceylon.language.model.InterfaceModel$impl;
import ceylon.language.model.Member;
import ceylon.language.model.Member$impl;
import ceylon.language.model.MemberInterface;
import ceylon.language.model.MemberInterface$impl;
import ceylon.language.model.Model$impl;
import ceylon.language.model.Type$impl;
import ceylon.language.model.declaration.InterfaceDeclaration;

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
public class FreeInterfaceWithAppliedMemberInterface<Container, Type> 
    extends FreeInterface
    implements ceylon.language.model.MemberInterface<Container, Type> {

    private MemberInterface<Container, Type> memberDelegate;
    @Ignore
    private TypeDescriptor $reifiedContainer;
    @Ignore
    private TypeDescriptor $reifiedType;

    public FreeInterfaceWithAppliedMemberInterface(@Ignore TypeDescriptor $reifiedContainer,
            @Ignore TypeDescriptor $reifiedType,
            Interface declaration) {
        super(declaration);
        this.$reifiedContainer = $reifiedContainer;
        this.$reifiedType = $reifiedType;
    }

    @Override
    @Ignore
    public Member$impl<Container, ceylon.language.model.Interface<? extends Type>> $ceylon$language$model$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public InterfaceModel$impl<Type> $ceylon$language$model$InterfaceModel$impl() {
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
    public Type$impl $ceylon$language$model$Type$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public MemberInterface$impl<Container, Type> $ceylon$language$model$MemberInterface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public ClassOrInterface$impl<Type> $ceylon$language$model$ClassOrInterface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void init() {
        super.init();
        memberDelegate = new AppliedMemberInterface($reifiedContainer, $reifiedType, ((com.redhat.ceylon.compiler.typechecker.model.Interface)declaration).getType());
    }

    @Override
    @Ignore
    public ceylon.language.model.Interface<? extends Type> $call() {
        checkInit();
        return memberDelegate.$call();
    }

    @Override
    @Ignore
    public ceylon.language.model.Interface<? extends Type> $call(Object arg0) {
        checkInit();
        return memberDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public ceylon.language.model.Interface<? extends Type> $call(Object arg0, Object arg1) {
        checkInit();
        return memberDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public ceylon.language.model.Interface<? extends Type> $call(Object arg0, Object arg1, Object arg2) {
        checkInit();
        return memberDelegate.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public ceylon.language.model.Interface<? extends Type> $call(Object... args) {
        checkInit();
        return memberDelegate.$call(args);
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        checkInit();
        return memberDelegate.$getVariadicParameterIndex();
    }

    @Override
    @TypeInfo("ceylon.language.model::ClassOrInterface<ceylon.language::Anything>")
    public ceylon.language.model.ClassOrInterface<? extends Object> getDeclaringClassOrInterface() {
        checkInit();
        return memberDelegate.getDeclaringClassOrInterface();
    }

    @Override
    @TypeInfo("ceylon.language.model::Member<Container,Kind>")
    @TypeParameters({ 
        @TypeParameter("Container"), 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.model::ClassOrInterface<ceylon.language::Anything>") 
    })
    public <Container, Kind extends ClassOrInterface<? extends Object>> Member<Container, Kind> memberApply(@Ignore TypeDescriptor $reifiedContainer, @Ignore TypeDescriptor $reifiedKind, @Name("types") @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Type>") @Sequenced Sequential<? extends ceylon.language.model.Type> types) {
        // TODO: check arguments
        return (Member)this;
    }
    
    @Override
    @TypeInfo("ceylon.language.model.declaration::ClassDeclaration")
    public InterfaceDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type")
    })
    @TypeInfo("ceylon.language.model::Attribute<SubType,Type>|ceylon.language::Null")
    public <SubType, Type>
        ceylon.language.model.Attribute<? super SubType, ? extends Type> getAttribute(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                          @Ignore TypeDescriptor $reifiedType, 
                                                                                          String name) {
        checkInit();
        return memberDelegate.<SubType, Type>getAttribute($reifiedSubType, $reifiedType, name);
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type")
    })
    @TypeInfo("ceylon.language.model::VariableAttribute<SubType,Type>|ceylon.language::Null")
    public <SubType, Type>
        ceylon.language.model.VariableAttribute<? super SubType, Type> getVariableAttribute(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                                @Ignore TypeDescriptor $reifiedType, 
                                                                                                String name) {
        checkInit();
        return memberDelegate.<SubType, Type>getVariableAttribute($reifiedSubType, $reifiedType, name);
    }

    @Override
    @Ignore
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Member<? super SubType, ? extends Kind> getClassOrInterface(TypeDescriptor arg0, TypeDescriptor arg1, String arg2) {
        checkInit();
        return memberDelegate.getClassOrInterface(arg0, arg1, arg2);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.model::Member<SubType,Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "SubType"), 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.model::ClassOrInterface<ceylon.language::Anything>")
    })
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Member<? super SubType, ? extends Kind> getClassOrInterface(@Ignore TypeDescriptor arg0, @Ignore TypeDescriptor arg1, @Name("name") @TypeInfo("ceylon.language::String") String arg2, @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Type>") Sequential<? extends ceylon.language.model.Type> arg3) {
        checkInit();
        return memberDelegate.getClassOrInterface(arg0, arg1, arg2, arg3);
    }

    @Override
    @Ignore
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Sequential<? extends ceylon.language.model.Type> getClassOrInterface$types(TypeDescriptor arg0, TypeDescriptor arg1, String arg2) {
        checkInit();
        return memberDelegate.getClassOrInterface$types(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public <SubType, Type, Arguments extends Sequential<? extends Object>>
    ceylon.language.model.Method<? super SubType, ? extends Type, ? super Arguments> getMethod(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                                   @Ignore TypeDescriptor $reifiedType, 
                                                                                                   @Ignore TypeDescriptor $reifiedArguments, 
                                                                                                   String name){
        checkInit();
        return memberDelegate.getMethod($reifiedSubType, $reifiedType, $reifiedArguments, name);
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @TypeInfo("ceylon.language.model::Method<SubType,Type,Arguments>|ceylon.language::Null")
    public <SubType, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.model.Method<? super SubType, ? extends Type, ? super Arguments> getMethod(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                                       @Ignore TypeDescriptor $reifiedType, 
                                                                                                       @Ignore TypeDescriptor $reifiedArguments, 
                                                                                                       String name, 
                                                                                                       @Name("types") @Sequenced Sequential<? extends ceylon.language.model.Type> types) {
        checkInit();
        return memberDelegate.getMethod($reifiedSubType, $reifiedType, $reifiedArguments, name, types);
    }

    @Override
    @Ignore
    public Sequential<? extends ceylon.language.model.Type> getMethod$types(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                @Ignore TypeDescriptor $reifiedType, 
                                                                                @Ignore TypeDescriptor $reifiedArguments, 
                                                                                String name){
        checkInit();
        return memberDelegate.getMethod$types($reifiedSubType, $reifiedType, $reifiedArguments, name);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Interface<ceylon.language::Anything>>")
    public Sequential<? extends ceylon.language.model.Interface<? extends Object>> getInterfaces() {
        checkInit();
        return memberDelegate.getInterfaces();
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.model::Class<ceylon.language::Anything,ceylon.language::Nothing>")
    public ceylon.language.model.Class getSuperclass() {
        checkInit();
        return memberDelegate.getSuperclass();
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.model.declaration::TypeParameter,ceylon.language.model::Type>")
    public Map<? extends ceylon.language.model.declaration.TypeParameter, ? extends ceylon.language.model.Type> getTypeArguments() {
        checkInit();
        return memberDelegate.getTypeArguments();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(FreeInterfaceWithAppliedMemberInterface.class, $reifiedContainer, $reifiedType);
    }
}
