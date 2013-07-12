package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.metamodel.Value;
import ceylon.language.metamodel.Class;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.ClassOrInterface$impl;
import ceylon.language.metamodel.DeclarationType$impl;
import ceylon.language.metamodel.Function;
import ceylon.language.metamodel.Interface$impl;
import ceylon.language.metamodel.InterfaceType$impl;
import ceylon.language.metamodel.Member;
import ceylon.language.metamodel.Type$impl;
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
    @TypeParameter(value = "Type", variance = Variance.OUT),
    })
public class FreeInterfaceWithType<Type>
    extends FreeInterface
    implements ceylon.language.metamodel.Interface<Type> {

    private AppliedInterfaceType<Type> typeDelegate;

    public FreeInterfaceWithType(@Ignore TypeDescriptor $reifiedType, Interface declaration) {
        super(declaration);
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
    public InterfaceType$impl<Type> $ceylon$language$metamodel$InterfaceType$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Interface$impl<Type> $ceylon$language$metamodel$Interface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void init() {
        super.init();
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        // FIXME: this is wrong because it does not include the container type
        com.redhat.ceylon.compiler.typechecker.model.ProducedType appliedClassType = declaration.getProducedReference(null, producedTypes).getType();
        typeDelegate = new AppliedInterfaceType<Type>(null, appliedClassType);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Member<SubType,Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "SubType"), 
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Value<ceylon.language::Anything>") 
    })
    public <SubType, Kind extends Value<? extends Object>> Member<SubType, Kind> getAttribute(@Ignore TypeDescriptor $reifiedSubType,
            @Ignore TypeDescriptor $reifiedKind, 
            @Name("name") @TypeInfo("ceylon.language::String") String name) {
        checkInit();
        return typeDelegate.getAttribute($reifiedSubType, $reifiedKind, name);
    }

    @Override
    @Ignore
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Member<SubType, Kind> getClassOrInterface(TypeDescriptor $reifiedSubType, 
            TypeDescriptor $reifiedKind, 
            String name) {
        checkInit();
        return typeDelegate.getClassOrInterface($reifiedSubType, $reifiedKind, name);
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
        return typeDelegate.getClassOrInterface($reifiedSubType, $reifiedKind, name, types);
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
    public <SubType, Kind extends Function> Member<SubType, Kind> getFunction(TypeDescriptor $reifiedSubType, 
            TypeDescriptor $reifiedKind, 
            String name) {
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
    public Sequential<? extends ceylon.language.metamodel.Interface<? extends Object>> getInterfaces() {
        checkInit();
        return typeDelegate.getInterfaces();
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.metamodel::Class<ceylon.language::Anything,ceylon.language::Nothing>")
    public Class<? extends Object, ? extends Object> getSuperclass() {
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
    @TypeInfo("ceylon.language.metamodel.declaration::InterfaceDeclaration")
    public InterfaceDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Interface<ceylon.language::Anything>")
    public ceylon.language.metamodel.Interface<? extends Object> apply(@Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") Sequential<? extends ceylon.language.metamodel.Type> types) {
        return this;
    }

    @Override
    public TypeDescriptor $getType() {
        checkInit();
        TypeDescriptor.Class type = (TypeDescriptor.Class) typeDelegate.$getType();
        TypeDescriptor[] args = type.getTypeArguments();
        return TypeDescriptor.klass(FreeInterfaceWithType.class, args[0]);
    }
}
