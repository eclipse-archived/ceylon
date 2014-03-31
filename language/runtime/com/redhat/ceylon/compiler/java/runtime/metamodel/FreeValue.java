package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.SetterDeclaration;
import ceylon.language.meta.declaration.ValueDeclaration$impl;

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
public class FreeValue 
    extends FreeFunctionOrValue
    implements ceylon.language.meta.declaration.ValueDeclaration, AnnotationBearing {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeValue.class);
    
    private OpenType type;

    private FreeSetter setter;

    protected FreeValue(com.redhat.ceylon.compiler.typechecker.model.Value declaration) {
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
    @TypeInfo("ceylon.language.meta.model::Value<Get,Set>")
    @TypeParameters({
        @TypeParameter("Get"),
        @TypeParameter("Set"),
    })
    public <Get, Set> ceylon.language.meta.model.Value<Get,Set> apply(@Ignore TypeDescriptor $reifiedGet,
                                                                      @Ignore TypeDescriptor $reifiedSet){
        if(!getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a member declaration with no container type: use memberApply");
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference typedReference = modelDecl.getProducedTypedReference(null, Collections.<ProducedType>emptyList());

        com.redhat.ceylon.compiler.typechecker.model.ProducedType getType = typedReference.getType();
        TypeDescriptor reifiedGet = Metamodel.getTypeDescriptorForProducedType(getType);
        // immutable values have Set=Nothing
        com.redhat.ceylon.compiler.typechecker.model.ProducedType setType = getVariable() ? 
                getType : modelDecl.getUnit().getNothingDeclaration().getType();
        TypeDescriptor reifiedSet = getVariable() ? reifiedGet : TypeDescriptor.NothingType;
        
        Metamodel.checkReifiedTypeArgument("apply", "Value<$1,$2>", 
                Variance.OUT, getType, $reifiedGet,
                Variance.IN, setType, $reifiedSet);
        return new AppliedValue<Get,Set>(reifiedGet, reifiedSet, this, typedReference, null, null);
    }

    @TypeInfo("ceylon.language.meta.model::Attribute<Container,Get,Set>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Get"),
        @TypeParameter("Set"),
    })
    @Override
    public <Container, Get, Set>
        ceylon.language.meta.model.Attribute<Container, Get, Set> memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedGet,
                @Ignore TypeDescriptor $reifiedSet,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Container> containerType){
        if(getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a toplevel declaration to a container type: use apply");
        ProducedType qualifyingType = Metamodel.getModel(containerType);
        Metamodel.checkQualifyingType(qualifyingType, declaration);
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference typedReference = modelDecl.getProducedTypedReference(qualifyingType, Collections.<ProducedType>emptyList());
        TypeDescriptor reifiedContainer = Metamodel.getTypeDescriptorForProducedType(qualifyingType);
        
        com.redhat.ceylon.compiler.typechecker.model.ProducedType getType = typedReference.getType();
        TypeDescriptor reifiedGet = Metamodel.getTypeDescriptorForProducedType(getType);
        // immutable values have Set=Nothing
        com.redhat.ceylon.compiler.typechecker.model.ProducedType setType = getVariable() ? 
                getType : modelDecl.getUnit().getNothingDeclaration().getType();
        TypeDescriptor reifiedSet = getVariable() ? reifiedGet : TypeDescriptor.NothingType;
        
        Metamodel.checkReifiedTypeArgument("memberApply", "Attribute<$1,$2,$3>", 
                Variance.IN, qualifyingType, $reifiedContainer,
                Variance.OUT, getType, $reifiedGet,
                Variance.IN, setType, $reifiedSet);
        return new AppliedAttribute<Container,Get,Set>(reifiedContainer, reifiedGet, reifiedSet, this, typedReference, containerType);
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::OpenType")
    public OpenType getOpenType() {
        return type;
    }

    @TypeInfo("ceylon.language::Anything")
    @Override
    public Object get(){
        return apply(Anything.$TypeDescriptor$, TypeDescriptor.NothingType).get();
    }

    @TypeInfo("ceylon.language::Anything")
    @Override
    public Object memberGet(@Name("container") @TypeInfo("ceylon.language::Object") Object container){
        ceylon.language.meta.model.Type<?> containerType = Metamodel.getAppliedMetamodel(Metamodel.getTypeDescriptor(container));
        return memberApply(TypeDescriptor.NothingType, Anything.$TypeDescriptor$, TypeDescriptor.NothingType, containerType).bind(container).get();
    }

    @Override
    public int hashCode() {
        return Metamodel.hashCode(this, "value");
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof FreeValue == false)
            return false;
        return Metamodel.equalsForSameType(this, (FreeValue)obj);
    }

    @TypeInfo("ceylon.language::Anything")
    @Override
    public Object set(@TypeInfo("ceylon.language::Anything") @Name("newValue") Object newValue){
        return apply(Anything.$TypeDescriptor$, TypeDescriptor.NothingType).$setIfAssignable(newValue);
    }

    @TypeInfo("ceylon.language::Anything")
    @Override
    public Object memberSet(@Name("container") @TypeInfo("ceylon.language::Object") Object container,
            @TypeInfo("ceylon.language::Anything") @Name("newValue") Object newValue){
        ceylon.language.meta.model.Type<?> containerType = Metamodel.getAppliedMetamodel(Metamodel.getTypeDescriptor(container));
        return memberApply(TypeDescriptor.NothingType, Anything.$TypeDescriptor$, TypeDescriptor.NothingType, containerType).bind(container).$setIfAssignable(newValue);
    }

    @Override
    public String toString() {
        return "value "+super.toString();
    }

    @TypeInfo("ceylon.language.meta.declaration::SetterDeclaration|ceylon.language::Null")
    @Override
    public SetterDeclaration getSetter() {
        if(setter == null && ((com.redhat.ceylon.compiler.typechecker.model.Value)declaration).getSetter() != null){
            synchronized(Metamodel.getLock()){
                if(setter == null){
                    // must be deferred because getter/setter refer to one another
                    com.redhat.ceylon.compiler.typechecker.model.Setter setterModel = ((com.redhat.ceylon.compiler.typechecker.model.Value)declaration).getSetter();
                    if(setterModel != null)
                        this.setter = (FreeSetter) Metamodel.getOrCreateMetamodel(setterModel);
                }
            }
        }
        return setter;
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        if(parameter != null
                && !parameter.getModel().isShared()){
            // get the annotations from the parameter itself
            Annotation[][] parameterAnnotations;
            Scope container = parameter.getModel().getContainer();
            if(container instanceof com.redhat.ceylon.compiler.typechecker.model.Method) {
                parameterAnnotations = Metamodel.getJavaMethod(
                        (com.redhat.ceylon.compiler.typechecker.model.Method)container)
                        .getParameterAnnotations();
            } else if(container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassAlias){
                parameterAnnotations = Reflection.findClassAliasInstantiator(
                        Metamodel.getJavaClass((com.redhat.ceylon.compiler.typechecker.model.Class)container),
                        (com.redhat.ceylon.compiler.typechecker.model.ClassAlias)container)
                        .getParameterAnnotations();
            } else if(container instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
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
