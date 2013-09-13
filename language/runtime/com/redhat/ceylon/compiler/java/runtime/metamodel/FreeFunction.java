package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.model.declaration.AnnotatedDeclaration;
import ceylon.language.model.declaration.FunctionDeclaration$impl;
import ceylon.language.model.declaration.FunctionalDeclaration$impl;
import ceylon.language.model.declaration.GenericDeclaration$impl;
import ceylon.language.model.declaration.OpenType;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeFunction 
    extends FreeFunctionOrValue
    implements ceylon.language.model.declaration.FunctionDeclaration, AnnotationBearing {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeFunction.class);
    
    private Sequential<? extends ceylon.language.model.declaration.TypeParameter> typeParameters;
    
    private OpenType type;

    private Sequential<? extends ceylon.language.model.declaration.FunctionOrValueDeclaration> parameterList;

    public FreeFunction(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);

        // FIXME: make lazy
        // FIXME: share with ClassOrInterface
        List<com.redhat.ceylon.compiler.typechecker.model.TypeParameter> typeParameters = ((Functional) declaration).getTypeParameters();
        ceylon.language.model.declaration.TypeParameter[] typeParametersArray = new ceylon.language.model.declaration.TypeParameter[typeParameters.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : typeParameters){
            typeParametersArray[i++] = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter(tp);
        }
        this.typeParameters = Util.sequentialInstance(ceylon.language.model.declaration.TypeParameter.$TypeDescriptor, typeParametersArray);
        
        this.type = Metamodel.getMetamodel(declaration.getType());
        
        List<ParameterList> parameterLists = ((Functional)declaration).getParameterLists();
        ParameterList parameterList = parameterLists.get(0);
        List<Parameter> modelParameters = parameterList.getParameters();
        ceylon.language.model.declaration.FunctionOrValueDeclaration[] parameters = new ceylon.language.model.declaration.FunctionOrValueDeclaration[modelParameters.size()];
        i=0;
        for(Parameter modelParameter : modelParameters){
            parameters[i] = (ceylon.language.model.declaration.FunctionOrValueDeclaration)Metamodel.getOrCreateMetamodel(modelParameter.getModel());
            i++;
        }
        this.parameterList = Util.sequentialInstance(ceylon.language.model.declaration.FunctionOrValueDeclaration.$TypeDescriptor, parameters);
    }

    @Override
    @Ignore
    public FunctionDeclaration$impl $ceylon$language$model$declaration$FunctionDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public FunctionalDeclaration$impl $ceylon$language$model$declaration$FunctionalDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public GenericDeclaration$impl $ceylon$language$model$declaration$GenericDeclaration$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.model.declaration::FunctionOrValueDeclaration>")
    public Sequential<? extends ceylon.language.model.declaration.FunctionOrValueDeclaration> getParameterDeclarations(){
        return parameterList;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::FunctionOrValueDeclaration|ceylon.language::Null")
    public ceylon.language.model.declaration.FunctionOrValueDeclaration getParameterDeclaration(@Name("name") String name){
        Iterator<?> iterator = parameterList.iterator();
        Object o;
        while((o = iterator.next()) != finished_.$get()){
            ceylon.language.model.declaration.FunctionOrValueDeclaration pd = (ceylon.language.model.declaration.FunctionOrValueDeclaration) o;
            if(pd.getName().equals(name))
                return pd;
        }
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.model.declaration::TypeParameter>")
    public Sequential<? extends ceylon.language.model.declaration.TypeParameter> getTypeParameterDeclarations() {
        return typeParameters;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::TypeParameter|ceylon.language::Null")
    public ceylon.language.model.declaration.TypeParameter getTypeParameterDeclaration(@Name("name") String name) {
        Iterator<? extends ceylon.language.model.declaration.TypeParameter> iterator = typeParameters.iterator();
        Object it;
        while((it = iterator.next()) != finished_.$get()){
            ceylon.language.model.declaration.TypeParameter tp = (ceylon.language.model.declaration.TypeParameter) it;
            if(tp.getName().equals(name))
                return tp;
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public <Return extends Object, Arguments extends Sequential<? extends Object>> ceylon.language.model.Function<Return, Arguments> apply(TypeDescriptor $reifiedReturn,
            TypeDescriptor $reifiedArguments){
        return apply($reifiedReturn,$reifiedArguments,(Sequential)empty_.$get());
    }

    @Override
    @TypeInfo("ceylon.language.model::Function<Return,Arguments>")
    @TypeParameters({
        @TypeParameter("Return"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    public <Return extends Object, Arguments extends Sequential<? extends Object>> ceylon.language.model.Function<Return, Arguments> apply(
            @Ignore TypeDescriptor $reifiedReturn,
            @Ignore TypeDescriptor $reifiedArguments,
            @Name("typeArguments") @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Type<ceylon.language::Anything>>") @Sequenced Sequential<? extends ceylon.language.model.Type<?>> typeArguments){
        if(!getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a member declaration with no container type: use memberApply");
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(typeArguments);
        Metamodel.checkTypeArguments(null, declaration, producedTypes);
        com.redhat.ceylon.compiler.typechecker.model.ProducedReference appliedFunction = declaration.getProducedReference(null, producedTypes);
        TypeDescriptor reifiedType = Metamodel.getTypeDescriptorForFunction(appliedFunction);
        TypeDescriptor reifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.getUnit(), (Functional) declaration, appliedFunction);
        return new AppliedFunction(reifiedType, reifiedArguments, appliedFunction, this, null, null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Ignore
    @Override
    public <Container, Return, Arguments extends Sequential<? extends Object>>
        ceylon.language.model.Method<Container, Return, Arguments> memberApply(TypeDescriptor $reifiedContainer,
                                                                               TypeDescriptor $reifiedReturn,
                                                                               TypeDescriptor $reifiedArguments,
                                                                               ceylon.language.model.Type<? extends Container> containerType){
        
        return this.<Container, Return, Arguments>memberApply($reifiedContainer,
                                                              $reifiedReturn,
                                                              $reifiedArguments,
                                                              containerType,
                                                              (Sequential)empty_.$get());
    }

    @TypeInfo("ceylon.language.model::Method<Container,Return,Arguments>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Return"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @Override
    public <Container, Return, Arguments extends Sequential<? extends Object>>
        ceylon.language.model.Method<Container, Return, Arguments> memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedReturn,
                @Ignore TypeDescriptor $reifiedArguments,
                @Name("containerType") ceylon.language.model.Type<? extends Container> containerType,
                @Name("typeArguments") @Sequenced Sequential<? extends ceylon.language.model.Type<?>> typeArguments){
        if(getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a toplevel declaration to a container type: use apply");
        return getAppliedMethod($reifiedContainer, $reifiedReturn, $reifiedArguments, typeArguments, containerType);
    }

    <Container, Type, Arguments extends ceylon.language.Sequential<? extends Object>>
    ceylon.language.model.Method<Container, Type, Arguments> getAppliedMethod(@Ignore TypeDescriptor $reifiedContainer, 
                                                                              @Ignore TypeDescriptor $reifiedType, 
                                                                              @Ignore TypeDescriptor $reifiedArguments, 
                                                                              Sequential<? extends ceylon.language.model.Type<?>> typeArguments,
                                                                              ceylon.language.model.Type<? extends Object> container){
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(typeArguments);
        ProducedType containerType = Metamodel.getModel(container);
        final ProducedTypedReference appliedFunction = ((com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration)declaration).getProducedTypedReference(containerType, producedTypes);
        TypeDescriptor reifiedType = Metamodel.getTypeDescriptorForFunction(appliedFunction);
        TypeDescriptor reifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.getUnit(), (Functional) declaration, appliedFunction);

        return new AppliedMethod($reifiedContainer, reifiedType, reifiedArguments, appliedFunction, this, container);
    }
    
    @Override
    @TypeInfo("ceylon.language.model.declaration::OpenType")
    public OpenType getOpenType() {
        return type;
    }

    @Override
    public boolean getAnnotation(){
        return declaration.isAnnotation();
    }

    @Override
    public int hashCode() {
        int result = 1;
        java.lang.Object container = getContainer();
        result = 37 * result + (container == null ? 0 : container.hashCode());
        result = 37 * result + getName().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.model.declaration.FunctionDeclaration == false)
            return false;
        ceylon.language.model.declaration.FunctionDeclaration other = (ceylon.language.model.declaration.FunctionDeclaration) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }

    @Override
    public String toString() {
        return "function "+super.toString();
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

    @Ignore
    @Override
    public java.lang.annotation.Annotation[] $getJavaAnnotations() {
        // FIXME: this could be a FunctionalParameter!
        return Metamodel.getJavaMethod((Method) declaration).getAnnotations();
    }
}
