package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.ValueDeclaration$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;

@Ceylon(major = 6)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeAttribute 
    extends FreeFunctionOrValue
    implements ceylon.language.meta.declaration.ValueDeclaration, AnnotationBearing {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeAttribute.class);
    
    private OpenType type;

    protected FreeAttribute(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);

        this.type = Metamodel.getMetamodel(declaration.getType());
    }

    @Override
    @Ignore
    public ValueDeclaration$impl $ceylon$language$meta$declaration$ValueDeclaration$impl() {
        return null;
    }

    @Override
    public boolean getVariable(){
        return ((com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration) declaration).isVariable();
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.model::Value<Type>")
    @TypeParameters({
        @TypeParameter("Type"),
    })
    public <Type> ceylon.language.meta.model.Value<Type> apply(@Ignore TypeDescriptor $reifiedType){
        if(!getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a member declaration with no container type: use memberApply");
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference typedReference = modelDecl.getProducedTypedReference(null, Collections.<ProducedType>emptyList());
        Metamodel.checkReifiedTypeArgument("apply", "Value<$1>", Variance.OUT, typedReference.getType(), $reifiedType);
        TypeDescriptor reifiedType = Metamodel.getTypeDescriptorForProducedType(typedReference.getType());
        return modelDecl.isVariable() 
                ? new AppliedVariable(reifiedType, this, typedReference, null, null) 
                : new AppliedValue(reifiedType, this, typedReference, null, null);
    }

    @TypeInfo("ceylon.language.meta.model::Attribute<Container,Type>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Type"),
    })
    @Override
    public <Container, Type>
        ceylon.language.meta.model.Attribute<Container, Type> memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedType,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Container> containerType){
        if(getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a toplevel declaration to a container type: use apply");
        ProducedType qualifyingType = Metamodel.getModel(containerType);
        Metamodel.checkQualifyingType(qualifyingType, declaration);
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference typedReference = modelDecl.getProducedTypedReference(qualifyingType, Collections.<ProducedType>emptyList());
        TypeDescriptor reifiedContainer = Metamodel.getTypeDescriptorForProducedType(qualifyingType);
        TypeDescriptor reifiedType = Metamodel.getTypeDescriptorForProducedType(typedReference.getType());
        Metamodel.checkReifiedTypeArgument("memberApply", "Attribute<$1,$2>", 
                Variance.IN, qualifyingType, $reifiedContainer,
                Variance.OUT, typedReference.getType(), $reifiedType);
        return modelDecl.isVariable() 
                ? new AppliedVariableAttribute(reifiedContainer, reifiedType, this, typedReference, containerType) 
                : new AppliedAttribute(reifiedContainer, reifiedType, this, typedReference, containerType);
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::OpenType")
    public OpenType getOpenType() {
        return type;
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
        if(obj instanceof ceylon.language.meta.declaration.ValueDeclaration == false)
            return false;
        ceylon.language.meta.declaration.ValueDeclaration other = (ceylon.language.meta.declaration.ValueDeclaration) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }

    @Override
    public String toString() {
        return "value "+super.toString();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations() {
        if(parameter != null
                && !parameter.getModel().isShared()){
            // get the annotations from the parameter itself
            Annotation[][] parameterAnnotations;
            Scope container = parameter.getModel().getContainer();
            if(container instanceof com.redhat.ceylon.compiler.typechecker.model.Method)
                parameterAnnotations = Metamodel.getJavaMethod((com.redhat.ceylon.compiler.typechecker.model.Method)container).getParameterAnnotations();
            else if(container instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
                // FIXME: pretty sure that's wrong because of synthetic params. See ReflectionMethod.getParameters
                parameterAnnotations = Reflection.findConstructor(Metamodel.getJavaClass((com.redhat.ceylon.compiler.typechecker.model.Class)container)).getParameterAnnotations();
            }else{
                throw new RuntimeException("Unsupported parameter container");
            }
            // now find the right parameter
            List<Parameter> parameters = ((com.redhat.ceylon.compiler.typechecker.model.Functional)container).getParameterLists().get(0).getParameters();
            int index = parameters.indexOf(parameter);
            if(index == -1)
                throw new RuntimeException("Parameter "+parameter+" not found in container "+parameter.getModel().getContainer());
            if(index >= parameterAnnotations.length)
                throw new RuntimeException("Parameter "+parameter+" index is greater than JVM parameters for "+parameter.getModel().getContainer());
            return parameterAnnotations[index];
        }else{
            Class<?> javaClass = Metamodel.getJavaClass(declaration);
            // FIXME: pretty sure this doesn't work with interop and fields
            return Reflection.getDeclaredGetter(javaClass, Naming.getGetterName(declaration)).getAnnotations();
        }
    }
}
