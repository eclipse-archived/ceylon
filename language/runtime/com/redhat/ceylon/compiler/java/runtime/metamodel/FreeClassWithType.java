package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.Callable;
import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.metamodel.Attribute;
import ceylon.language.metamodel.Class$impl;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.ClassOrInterface$impl;
import ceylon.language.metamodel.DeclarationType$impl;
import ceylon.language.metamodel.Function;
import ceylon.language.metamodel.Interface;
import ceylon.language.metamodel.Member;
import ceylon.language.metamodel.Type$impl;
import ceylon.language.metamodel.declaration.ClassDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Class;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
})
public class FreeClassWithType<Type, Arguments extends Sequential<? extends Object>> 
    extends FreeClass 
    implements ceylon.language.metamodel.Class<Type, Arguments>, Callable<Type> {

    private AppliedClassType<Type, Arguments> typeDelegate;

    public FreeClassWithType(@Ignore TypeDescriptor $reifiedType,
            @Ignore TypeDescriptor $reifiedArguments,
            Class declaration) {
        super(declaration);
    }

    @Override
    protected void init() {
        super.init();
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        // FIXME: this is wrong because it does not include the container type
        com.redhat.ceylon.compiler.typechecker.model.ProducedType appliedClassType = declaration.getProducedReference(null, producedTypes).getType();
        typeDelegate = new AppliedClassType<Type, Arguments>(null, null, appliedClassType, null);
    }
    
    @Override
    @Ignore
    public ClassOrInterface$impl<Type> $ceylon$language$metamodel$ClassOrInterface$impl() {
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
    public Class$impl<Type, Arguments> $ceylon$language$metamodel$Class$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Member<SubType,Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "SubType"), 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Attribute<ceylon.language::Anything>")
    })
    public <SubType, Kind extends Attribute<? extends Object>> Member<SubType, Kind> getAttribute(@Ignore TypeDescriptor $reifiedSubType, 
            @Ignore TypeDescriptor $reifiedKind, 
            @Name("name") @TypeInfo("ceylon.language::String") String name) {
        checkInit();
        return typeDelegate.<SubType,Kind>getAttribute($reifiedSubType, $reifiedKind, name);
    }

    @Override
    @Ignore
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Member<SubType, Kind> getClassOrInterface(TypeDescriptor $reifiedSubType, 
            TypeDescriptor $reifiedKind, 
            String name) {
        checkInit();
        return typeDelegate.<SubType,Kind>getClassOrInterface($reifiedSubType, $reifiedKind, name);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Member<SubType,Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::ClassOrInterface<ceylon.language::Anything>")
    })
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Member<SubType, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedSubType,
            @Ignore TypeDescriptor $reifiedKind, 
            @Name("name") @TypeInfo("ceylon.language::String") String name, 
            @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") Sequential<? extends ceylon.language.metamodel.Type> types) {
        checkInit();
        return typeDelegate.<SubType,Kind>getClassOrInterface($reifiedSubType, $reifiedKind, name, types);
    }

    @Override
    @Ignore
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Sequential<? extends ceylon.language.metamodel.Type> getClassOrInterface$types(TypeDescriptor $reifiedSubType, 
            TypeDescriptor $reifiedKind, 
            String name) {
        checkInit();
        return typeDelegate.getClassOrInterface$types($reifiedSubType, $reifiedKind, name);
    }

    @Override
    @Ignore
    public <SubType, Kind extends Function> Member<SubType, Kind> getFunction(TypeDescriptor $reifiedSubType, TypeDescriptor $reifiedKind, String name) {
        checkInit();
        return typeDelegate.getFunction($reifiedSubType, $reifiedKind, name);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Member<SubType,Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Function<ceylon.language::Anything,ceylon.language::Nothing>")
    })
    public <SubType, Kind extends Function> Member<SubType, Kind> getFunction(@Ignore TypeDescriptor $reifiedSubType, 
            @Ignore TypeDescriptor $reifiedKind, 
            @Name("name") @TypeInfo("ceylon.language::String") String name,
            @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") Sequential<? extends ceylon.language.metamodel.Type> types) {
        checkInit();
        return typeDelegate.getFunction($reifiedSubType, $reifiedKind, name, types);
    }

    @Override
    @Ignore
    public <SubType, Kind extends Function> Sequential<? extends ceylon.language.metamodel.Type> getFunction$types(TypeDescriptor $reifiedSubType,
            TypeDescriptor $reifiedKind, 
            String name) {
        checkInit();
        return typeDelegate.getFunction$types($reifiedSubType, $reifiedKind, name);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Interface<ceylon.language::Anything>>")
    public Sequential<? extends Interface<? extends Object>> getInterfaces() {
        checkInit();
        return typeDelegate.getInterfaces();
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Class<ceylon.language::Anything,ceylon.language::Nothing>")
    public ceylon.language.metamodel.Class<? extends Object, ? extends Object> getSuperclass() {
        checkInit();
        return typeDelegate.getSuperclass();
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.metamodel.declaration::TypeParameter,ceylon.language.metamodel::Type>")
    public Map<? extends ceylon.language.metamodel.declaration.TypeParameter, ? extends ceylon.language.metamodel.Type> getTypeArguments() {
        checkInit();
        return typeDelegate.getTypeArguments();
    }

    @Override
    @Ignore
    public Type $call() {
        checkInit();
        return typeDelegate.$call();
    }

    @Override
    @Ignore
    public Type $call(Object arg0) {
        checkInit();
        return typeDelegate.$call(arg0);
    }

    @Override
    @Ignore
    public Type $call(Object arg0, Object arg1) {
        checkInit();
        return typeDelegate.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public Type $call(Object arg0, Object arg1, Object arg2) {
        checkInit();
        return typeDelegate.$call(arg0, arg1, arg1);
    }

    @Override
    @Ignore
    public Type $call(Object... args) {
        checkInit();
        return typeDelegate.$call(args);
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        checkInit();
        return typeDelegate.$getVariadicParameterIndex();
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::ClassDeclaration")
    public ClassDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Class<ceylon.language::Anything,ceylon.language::Nothing>")
    public ceylon.language.metamodel.Class<? extends Object, ? super Sequential<? extends Object>> apply(@Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") Sequential<? extends ceylon.language.metamodel.Type> types) {
        // TODO: check arguments
        return (ceylon.language.metamodel.Class) this;
    }

    @Override
    public TypeDescriptor $getType() {
        TypeDescriptor.Class type = (TypeDescriptor.Class) typeDelegate.$getType();
        TypeDescriptor[] args = type.getTypeArguments();
        return TypeDescriptor.klass(FreeClassWithType.class, args[0], args[1]);
    }
}
