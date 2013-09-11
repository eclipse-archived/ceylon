package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import ceylon.language.model.declaration.AnnotatedDeclaration;
import ceylon.language.model.declaration.OpenType;
import ceylon.language.model.declaration.ValueDeclaration$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.Scope;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeAttribute 
    extends FreeFunctionOrValue
    implements ceylon.language.model.declaration.ValueDeclaration, AnnotationBearing {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeAttribute.class);
    
    private OpenType type;

    protected FreeAttribute(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);

        this.type = Metamodel.getMetamodel(declaration.getType());
    }

    @Override
    @Ignore
    public ValueDeclaration$impl $ceylon$language$model$declaration$ValueDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object apply$instance() {
        return null;
    }

    @Override
    public ceylon.language.model.Value<? extends Object> apply() {
        return apply(null);
    }
    
    @Override
    @TypeInfo("ceylon.language.model::Value<ceylon.language::Anything>")
    public ceylon.language.model.Value<? extends Object> apply(@Name @TypeInfo("ceylon.language::Anything") Object instance) {
        // FIXME: validate that instance is null for toplevels and not null for memberss
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        // FIXME: this is not valid if the container type has TP
        TypeDescriptor reifiedType = Metamodel.getTypeDescriptorForProducedType(modelDecl.getType());
        ProducedTypedReference typedReference = modelDecl.getProducedTypedReference(null, Collections.<ProducedType>emptyList());
        return modelDecl.isVariable() 
            ? new AppliedVariable(reifiedType, this, typedReference, null/*FIXME*/, instance) 
            : new AppliedValue(reifiedType, this, typedReference, null/*FIXME*/, instance);
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::OpenType")
    public OpenType getOpenType() {
        return type;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        AnnotatedDeclaration container = getContainer();
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
        if(obj instanceof ceylon.language.model.declaration.ValueDeclaration == false)
            return false;
        ceylon.language.model.declaration.ValueDeclaration other = (ceylon.language.model.declaration.ValueDeclaration) obj;
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
