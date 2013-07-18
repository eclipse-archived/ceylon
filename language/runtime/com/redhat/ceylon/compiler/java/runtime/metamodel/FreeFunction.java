package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.declaration.FunctionDeclaration$impl;
import ceylon.language.metamodel.declaration.FunctionalDeclaration$impl;
import ceylon.language.metamodel.declaration.GenericDeclaration$impl;
import ceylon.language.metamodel.declaration.OpenType;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeFunction 
    extends FreeTopLevelOrMemberDeclaration
    implements ceylon.language.metamodel.declaration.FunctionDeclaration, AnnotationBearing {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeFunction.class);
    
    private Sequential<? extends ceylon.language.metamodel.declaration.TypeParameter> typeParameters;
    
    private OpenType type;

    private Sequential<? extends ceylon.language.metamodel.declaration.ParameterDeclaration> parameterList;

    public FreeFunction(com.redhat.ceylon.compiler.typechecker.model.Method declaration) {
        super(declaration);

        // FIXME: make lazy
        // FIXME: share with ClassOrInterface
        List<com.redhat.ceylon.compiler.typechecker.model.TypeParameter> typeParameters = declaration.getTypeParameters();
        ceylon.language.metamodel.declaration.TypeParameter[] typeParametersArray = new ceylon.language.metamodel.declaration.TypeParameter[typeParameters.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : typeParameters){
            typeParametersArray[i++] = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter(tp);
        }
        this.typeParameters = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.declaration.TypeParameter.$TypeDescriptor, typeParametersArray);
        
        this.type = Metamodel.getMetamodel(declaration.getType());
        
        List<ParameterList> parameterLists = declaration.getParameterLists();
        String methodName = Metamodel.getJavaMethodName(declaration);
        Annotation[][] parameterAnnotations = Metamodel.getJavaMethod(declaration).getParameterAnnotations();
        ParameterList parameterList = parameterLists.get(0);
        List<Parameter> modelParameters = parameterList.getParameters();
        ceylon.language.metamodel.declaration.ParameterDeclaration[] parameters = new ceylon.language.metamodel.declaration.ParameterDeclaration[modelParameters.size()];
        i=0;
        for(Parameter modelParameter : modelParameters){
            parameters[i] = new FreeParameter(modelParameter, parameterAnnotations[i]);
            i++;
        }
        this.parameterList = Util.sequentialInstance(ceylon.language.metamodel.declaration.ParameterDeclaration.$TypeDescriptor, parameters);
    }

    @Override
    @Ignore
    public FunctionDeclaration$impl $ceylon$language$metamodel$declaration$FunctionDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public FunctionalDeclaration$impl $ceylon$language$metamodel$declaration$FunctionalDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public GenericDeclaration$impl $ceylon$language$metamodel$declaration$GenericDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.declaration::ParameterDeclaration>")
    public Sequential<? extends ceylon.language.metamodel.declaration.ParameterDeclaration> getParameterDeclarations(){
        return parameterList;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.declaration::TypeParameter>")
    public Sequential<? extends ceylon.language.metamodel.declaration.TypeParameter> getTypeParameterDeclarations() {
        return typeParameters;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::TypeParameter|ceylon.language::Null")
    public ceylon.language.metamodel.declaration.TypeParameter getTypeParameterDeclaration(@Name("name") String name) {
        Iterator<? extends ceylon.language.metamodel.declaration.TypeParameter> iterator = typeParameters.iterator();
        Object it;
        while((it = iterator.next()) != finished_.$get()){
            ceylon.language.metamodel.declaration.TypeParameter tp = (ceylon.language.metamodel.declaration.TypeParameter) it;
            if(tp.getName().equals(name))
                return tp;
        }
        return null;
    }

    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.Type> apply$types(){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public ceylon.language.metamodel.Function<? extends Object, ? super Sequential<? extends Object>> apply(){
        return apply(apply$types());
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Function<ceylon.language::Anything,ceylon.language::Nothing>")
    public ceylon.language.metamodel.Function<? extends Object, ? super Sequential<? extends Object>> apply(@Name("types") @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types){
        return bindAndApply(null, types);
    }

    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.Type> bindAndApply$types(Object instance){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public ceylon.language.metamodel.Function<? extends Object, ? super Sequential<? extends Object>> bindAndApply(Object instance){
        return bindAndApply(instance, bindAndApply$types(instance));
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Function<ceylon.language::Anything,ceylon.language::Nothing>")
    public ceylon.language.metamodel.Function<? extends Object, ? super Sequential<? extends Object>> bindAndApply(
            @Name("instance") @TypeInfo("ceylon.language::Object") Object instance, 
            @Name("types") @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Type>") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types){
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        // FIXME: should have the container type
        com.redhat.ceylon.compiler.typechecker.model.ProducedReference appliedFunction = declaration.getProducedReference(null, producedTypes);
        TypeDescriptor reifiedType = Metamodel.getTypeDescriptorForFunction(appliedFunction);
        TypeDescriptor reifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.getUnit(), (Functional) declaration, appliedFunction);
        return new AppliedFunction(reifiedType, reifiedArguments, appliedFunction, this, instance);
    }

    @Ignore
    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
        Sequential<? extends ceylon.language.metamodel.Type> memberApply$types(TypeDescriptor $reifiedContainer,
                                                                               TypeDescriptor $reifiedType,
                                                                               TypeDescriptor $reifiedArguments){
        
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.metamodel.Method<Container, Type, Arguments> memberApply(TypeDescriptor $reifiedContainer,
                                                                                 TypeDescriptor $reifiedType,
                                                                                 TypeDescriptor $reifiedArguments){
        
        return this.<Container, Type, Arguments>memberApply($reifiedContainer,
                                                            $reifiedType,
                                                            $reifiedArguments,
                                                            this.<Container, Type, Arguments>memberApply$types($reifiedContainer, $reifiedType, $reifiedArguments));
    }

    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.metamodel.Method<Container, Type, Arguments> memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedType,
                @Ignore TypeDescriptor $reifiedArguments,
                @Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types){
        // FIXME: container?
        return getAppliedMethod($reifiedContainer, $reifiedType, $reifiedArguments, types, null);
    }

    <Container, Type, Arguments extends ceylon.language.Sequential<? extends Object>>
    ceylon.language.metamodel.Method<Container, Type, Arguments> getAppliedMethod(@Ignore TypeDescriptor $reifiedContainer, 
                                                                                  @Ignore TypeDescriptor $reifiedType, 
                                                                                  @Ignore TypeDescriptor $reifiedArguments, 
                                                                                  Sequential<? extends ceylon.language.metamodel.Type> types,
                                                                                  ceylon.language.metamodel.ClassOrInterface<? extends Object> container){
        // if we don't have any TP our declaration will also be a Method
        if(!Metamodel.hasTypeParameters((Generic) declaration))
            return (ceylon.language.metamodel.Method)Metamodel.getOrCreateMetamodel(declaration);
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        // FIXME: check this null qualifying type
        // this is most likely wrong as it doesn't seem to substitute the containing type parameters
        ProducedType containerType = null;
        if(container != null){
            containerType = Metamodel.getModel(container);
        }
        final ProducedTypedReference appliedFunction = ((com.redhat.ceylon.compiler.typechecker.model.Method)declaration).getProducedTypedReference(containerType, producedTypes);
        return new AppliedMethod($reifiedContainer, $reifiedType, $reifiedArguments, appliedFunction, this, container);
    }
    
    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::OpenType")
    public OpenType getOpenType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

    @Ignore
    @Override
    public java.lang.annotation.Annotation[] $getJavaAnnotations() {
        return Metamodel.getJavaMethod((Method) declaration).getAnnotations();
    }
}
