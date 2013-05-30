package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.declaration.Declaration$impl;
import ceylon.language.metamodel.declaration.FunctionDeclaration$impl;
import ceylon.language.metamodel.declaration.GenericDeclaration$impl;
import ceylon.language.metamodel.declaration.OpenType;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeFunction 
    extends FreeDeclaration
    implements ceylon.language.metamodel.declaration.FunctionDeclaration, AnnotationBearing {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeFunction.class);
    
    private Sequential<? extends ceylon.language.metamodel.declaration.TypeParameter> typeParameters;
    
    private OpenType type;

    private Sequence<? extends Sequential<? extends ceylon.language.metamodel.declaration.Parameter>> parameterLists;

    public FreeFunction(com.redhat.ceylon.compiler.typechecker.model.Method declaration) {
        super(declaration);

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
        ceylon.language.Sequential[] parameterListsArray = new ceylon.language.Sequential[parameterLists.size()];
        int p=0;
        String methodName = Metamodel.getJavaMethodName(declaration);
        Annotation[][] parameterAnnotations = findMethod(Metamodel.getJavaClass(declaration), methodName).getParameterAnnotations();
        for(ParameterList parameterList : parameterLists){
            List<Parameter> modelParameters = parameterList.getParameters();
            ceylon.language.metamodel.declaration.Parameter[] parameters = new ceylon.language.metamodel.declaration.Parameter[modelParameters.size()];
            i=0;
            for(Parameter modelParameter : modelParameters){
                parameters[i] = new FreeParameter(modelParameter, parameterAnnotations[i]);
                i++;
            }
            parameterListsArray[p++] = Util.sequentialInstance(ceylon.language.metamodel.declaration.Parameter.$TypeDescriptor, parameters);
        }
        this.parameterLists = (Sequence)Util.sequentialInstance(TypeDescriptor.klass(Sequential.class, ceylon.language.metamodel.declaration.Parameter.$TypeDescriptor), parameterListsArray);
    }

    @Override
    @Ignore
    public FunctionDeclaration$impl $ceylon$language$metamodel$declaration$FunctionDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Declaration$impl $ceylon$language$metamodel$declaration$Declaration$impl() {
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
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.declaration::Parameter>")
    public Sequential<? extends ceylon.language.metamodel.declaration.Parameter> getParameters(){
        return parameterLists.getFirst();
    }

    @Override
    @TypeInfo("ceylon.language::Sequence<ceylon.language::Sequential<ceylon.language.metamodel.declaration::Parameter>>")
    public Sequence<? extends Sequential<? extends ceylon.language.metamodel.declaration.Parameter>> getParameterLists(){
        return parameterLists;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.declaration::TypeParameter>")
    public Sequential<? extends ceylon.language.metamodel.declaration.TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::TypeParameter|ceylon.language::Null")
    public ceylon.language.metamodel.declaration.TypeParameter getTypeParameter(@Name("name") String name) {
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
    public ceylon.language.metamodel.Function<? extends Object, ? super Sequential<? extends Object>> apply(@Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types){
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
        com.redhat.ceylon.compiler.typechecker.model.ProducedReference appliedFunction = declaration.getProducedReference(null, producedTypes);
        return new AppliedFunction(appliedFunction, this, instance);
    }

    @Ignore
    @Override
    public <Container, 
            Kind extends ceylon.language.metamodel.Function>
        Sequential<? extends ceylon.language.metamodel.Type> memberApply$types(TypeDescriptor $reifiedContainer,
                                                                                      TypeDescriptor $reifiedKind){
        
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public <Container, 
            Kind extends ceylon.language.metamodel.Function>
        ceylon.language.metamodel.Member<Container, Kind> memberApply(TypeDescriptor $reifiedContainer,
                                                                      TypeDescriptor $reifiedKind){
        
        return this.<Container, Kind>memberApply($reifiedContainer,
                                                 $reifiedKind,
                                                 this.<Container, Kind>memberApply$types($reifiedContainer, $reifiedKind));
    }

    @Override
    public <Container, 
            Kind extends ceylon.language.metamodel.Function>
        ceylon.language.metamodel.Member<Container, Kind> memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedKind,
                @Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types){
        // FIXME: check this
        AppliedClassOrInterfaceType<Container> containerType = (AppliedClassOrInterfaceType<Container>) Metamodel.getAppliedMetamodel($reifiedContainer);
        return getAppliedFunction($reifiedContainer, $reifiedKind, types, containerType);
    }

    <Type, Kind extends ceylon.language.metamodel.Function>
    ceylon.language.metamodel.Member<Type, Kind> getAppliedFunction(TypeDescriptor $reifiedType, TypeDescriptor $reifiedKind, 
                                                                    Sequential<? extends ceylon.language.metamodel.Type> types,
                                                                    AppliedClassOrInterfaceType<Type> container){
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        final ProducedReference appliedFunction = declaration.getProducedReference(null, producedTypes);
        return new AppliedMember<Type, Kind>($reifiedType, $reifiedKind, container){
            @Override
            protected Kind bindTo(Object instance) {
                return (Kind) new AppliedFunction(appliedFunction, FreeFunction.this, instance);
            }
        };
    }
    
    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::OpenType")
    public OpenType getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

    @Ignore
    @Override
    public java.lang.annotation.Annotation[] $getJavaAnnotations() {
        Class<?> javaClass = Metamodel.getJavaClass(declaration);
        String name = Naming.selector((TypedDeclaration)declaration, 0);
        java.lang.reflect.Method best = findMethod(javaClass, name);
        return best.getAnnotations();
    }

    private static java.lang.reflect.Method findMethod(Class<?> javaClass, String methodName) {
        // How to find the right Method, just go for the one with the longest parameter list?
        // OR go via the Method in AppliedFunction?
        java.lang.reflect.Method best = null;
        int numBestParams = -1;
        int numBest = 0;
        for (java.lang.reflect.Method meth : javaClass.getDeclaredMethods()) {
            if (!meth.getName().equals(methodName)
                    || meth.isBridge() 
                    || meth.isSynthetic()
                    || meth.getAnnotation(Ignore.class) != null) {
                continue;
            }
            
            Class<?>[] parameterTypes = meth.getParameterTypes();
            if (parameterTypes.length > numBestParams) {
                best = meth;
                numBestParams = parameterTypes.length;
                numBest = 1;
            } else if (parameterTypes.length == numBestParams) {
                numBest++;
            }
        }
        if (best == null) {
            throw new RuntimeException("Couldn't find method " + javaClass + "." + methodName);
        }
        if (numBest > 1) {
            throw new RuntimeException("Method arity ambiguity " + javaClass + "." + methodName);
        }
        return best;
    }
}
