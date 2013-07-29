package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.Collections;
import java.util.List;

import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.model.Class;
import ceylon.language.model.ClassOrInterface;
import ceylon.language.model.ClassOrInterface$impl;
import ceylon.language.model.Interface$impl;
import ceylon.language.model.InterfaceModel$impl;
import ceylon.language.model.Member;
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
    @TypeParameter(value = "Type", variance = Variance.OUT),
    })
public class FreeInterfaceWithAppliedInterface<Type>
    extends FreeInterface
    implements ceylon.language.model.Interface<Type> {

    private AppliedInterface<Type> typeDelegate;

    public FreeInterfaceWithAppliedInterface(@Ignore TypeDescriptor $reifiedType, Interface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ClassOrInterface$impl<Type> $ceylon$language$model$ClassOrInterface$impl() {
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
    public InterfaceModel$impl<Type> $ceylon$language$model$InterfaceModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Interface$impl<Type> $ceylon$language$model$Interface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void init() {
        super.init();
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Collections.emptyList();
        // FIXME: this is wrong because it does not include the container type
        com.redhat.ceylon.compiler.typechecker.model.ProducedType appliedClassType = declaration.getProducedReference(null, producedTypes).getType();
        TypeDescriptor reifiedType = Metamodel.getTypeDescriptorForProducedType(appliedClassType);
        typeDelegate = new AppliedInterface<Type>(reifiedType, appliedClassType);
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type")
    })
    @TypeInfo("ceylon.language.model::Attribute<SubType,Type>|ceylon.language::Null")
    public <SubType, Type>
        ceylon.language.model.Attribute<SubType, Type> getAttribute(@Ignore TypeDescriptor $reifiedSubType, 
                                                                        @Ignore TypeDescriptor $reifiedType, 
                                                                        String name) {
        checkInit();
        return typeDelegate.<SubType, Type>getAttribute($reifiedSubType, $reifiedType, name);
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type")
    })
    @TypeInfo("ceylon.language.model::VariableAttribute<SubType,Type>|ceylon.language::Null")
    public <SubType, Type>
        ceylon.language.model.VariableAttribute<SubType, Type> getVariableAttribute(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                        @Ignore TypeDescriptor $reifiedType, 
                                                                                        String name) {
        checkInit();
        return typeDelegate.<SubType, Type>getVariableAttribute($reifiedSubType, $reifiedType, name);
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
    @TypeInfo("ceylon.language::Null|ceylon.language.model::Member<SubType,Kind>")
    @TypeParameters({ 
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.model::ClassOrInterface<ceylon.language::Anything>")
    })
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Member<SubType, Kind> getClassOrInterface(@Ignore TypeDescriptor $reifiedSubType,
            @Ignore TypeDescriptor $reifiedKind, 
            @Name("name") @TypeInfo("ceylon.language::String") String name,
            @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Type>") Sequential<? extends ceylon.language.model.Type> types) {
        checkInit();
        return typeDelegate.getClassOrInterface($reifiedSubType, $reifiedKind, name, types);
    }

    @Override
    @Ignore
    public <SubType, Kind extends ClassOrInterface<? extends Object>> Sequential<? extends ceylon.language.model.Type> getClassOrInterface$types(TypeDescriptor $reifiedSubType, 
            TypeDescriptor $reifiedKind, 
            String name) {
        checkInit();
        return typeDelegate.getClassOrInterface$types($reifiedSubType, $reifiedKind, name);
    }

    @Override
    @Ignore
    public <SubType, Type, Arguments extends Sequential<? extends Object>>
    ceylon.language.model.Method<SubType, Type, Arguments> getMethod(@Ignore TypeDescriptor $reifiedSubType, 
                                                                         @Ignore TypeDescriptor $reifiedType, 
                                                                         @Ignore TypeDescriptor $reifiedArguments, 
                                                                         String name){
        checkInit();
        return typeDelegate.getMethod($reifiedSubType, $reifiedType, $reifiedArguments, name);
    }

    @Override
    @TypeParameters({
        @TypeParameter(value = "SubType"),
        @TypeParameter(value = "Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @TypeInfo("ceylon.language.model::Method<SubType,Type,Arguments>|ceylon.language::Null")
    public <SubType, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.model.Method<SubType, Type, Arguments> getMethod(@Ignore TypeDescriptor $reifiedSubType, 
                                                                             @Ignore TypeDescriptor $reifiedType, 
                                                                             @Ignore TypeDescriptor $reifiedArguments, 
                                                                             String name, 
                                                                             @Name("types") @Sequenced Sequential<? extends ceylon.language.model.Type> types) {
        checkInit();
        return typeDelegate.getMethod($reifiedSubType, $reifiedType, $reifiedArguments, name, types);
    }

    @Override
    @Ignore
    public Sequential<? extends ceylon.language.model.Type> getMethod$types(@Ignore TypeDescriptor $reifiedSubType, 
                                                                                @Ignore TypeDescriptor $reifiedType, 
                                                                                @Ignore TypeDescriptor $reifiedArguments, 
                                                                                String name){
        checkInit();
        return typeDelegate.getMethod$types($reifiedSubType, $reifiedType, $reifiedArguments, name);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Interface<ceylon.language::Anything>>")
    public Sequential<? extends ceylon.language.model.Interface<? extends Object>> getInterfaces() {
        checkInit();
        return typeDelegate.getInterfaces();
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language.model::Class<ceylon.language::Anything,ceylon.language::Nothing>")
    public Class<? extends Object, ? extends Object> getSuperclass() {
        checkInit();
        return typeDelegate.getSuperclass();
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.model.declaration::TypeParameter,ceylon.language.model::Type>")
    public Map<? extends ceylon.language.model.declaration.TypeParameter, ? extends ceylon.language.model.Type> getTypeArguments() {
        checkInit();
        return typeDelegate.getTypeArguments();
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::InterfaceDeclaration")
    public InterfaceDeclaration getDeclaration() {
        return this;
    }

    @Override
    @TypeInfo("ceylon.language.model::Interface<ceylon.language::Anything>")
    public ceylon.language.model.Interface<? extends Object> apply(@Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Type>") Sequential<? extends ceylon.language.model.Type> types) {
        return this;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        checkInit();
        TypeDescriptor.Class type = (TypeDescriptor.Class) typeDelegate.$getType();
        TypeDescriptor[] args = type.getTypeArguments();
        return TypeDescriptor.klass(FreeInterfaceWithAppliedInterface.class, args[0]);
    }
}
