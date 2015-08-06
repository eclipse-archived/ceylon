package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.reflect.AnnotatedElement;
import java.util.Collections;

import ceylon.language.Sequential;
import ceylon.language.meta.declaration.ClassDeclaration;
import ceylon.language.meta.declaration.FunctionOrValueDeclaration;
import ceylon.language.meta.declaration.Module;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.Package;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.declaration.ValueConstructorDeclaration$impl;
import ceylon.language.meta.model.MemberClassValueConstructor;
import ceylon.language.meta.model.ValueConstructor;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor.Nothing;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeValueConstructor 
        extends FreeValue
        implements ValueConstructorDeclaration, 
                AnnotationBearing,
                ReifiedType {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeValueConstructor.class);
    
    final Constructor constructor;
    
    public FreeValueConstructor(Value value,
            com.redhat.ceylon.model.typechecker.model.Constructor constructor) {
        super(value);
        this.constructor = constructor;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Annotation>")
    @TypeParameters(@TypeParameter(value = "Annotation", satisfies = "ceylon.language::Annotation"))
    public <Annotation extends java.lang.annotation.Annotation> Sequential<? extends Annotation> annotations(@Ignore TypeDescriptor $reifiedAnnotation) {
        return Metamodel.annotations($reifiedAnnotation, this);
    }

    @Override
    public String getName() {
        return constructor.getName() == null ? "" : constructor.getName();
    }

    @Override
    public String getQualifiedName() {
        String name = getName();
        return ((Class)constructor.getContainer()).getQualifiedNameString() + (name.isEmpty() ? "" : "." + getName());
    }

    @Override
    public OpenType getOpenType() {
        return Metamodel.getMetamodel(constructor.getType());
    }

    @Override
    public Module getContainingModule() {
        return getContainer().getContainingModule();
    }

    @Override
    public Package getContainingPackage() {
        return getContainer().getContainingPackage();
    }

    @Override
    public boolean getToplevel() {
        return false;
    }

    @Override
    public ClassDeclaration getContainer() {
        return ((ClassDeclaration)Metamodel.getOrCreateMetamodel(((Class)constructor.getContainer())));
    }

    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        return Metamodel.getJavaConstructor(constructor).getAnnotations();
    }
    
    @Override
    @Ignore
    public boolean $isAnnotated$(java.lang.Class<? extends java.lang.annotation.Annotation> annotationType) {
        final AnnotatedElement element = Metamodel.getJavaConstructor(constructor);
        return element != null ? element.isAnnotationPresent(annotationType) : false;
    }
    
    @Override
    public <AnnotationType extends java.lang.annotation.Annotation> boolean annotated(TypeDescriptor reifed$AnnotationType) {
        return Metamodel.isAnnotated(reifed$AnnotationType, this);
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    @Override
    public String toString() {
        return "new "+getQualifiedName();
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof FreeValueConstructor) {
            return getContainer().equals(((FreeValueConstructor)other).getContainer())
                    && getName().equals(((FreeValueConstructor)other).getName());
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return getContainer().hashCode() ^ getName().hashCode();
    }

    @Override
    public ValueConstructorDeclaration$impl $ceylon$language$meta$declaration$ValueConstructorDeclaration$impl() {
        return null;
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.model::Value<Get,Set>")
    @TypeParameters({
        @TypeParameter("Get"),
        @TypeParameter("Set"),
    })
    public <Get, Set> ceylon.language.meta.model.Value<Get,Set> apply(@Ignore TypeDescriptor $reifiedGet,
                                                                      @Ignore TypeDescriptor $reifiedSet){
        // TODO if(!getToplevel())
        //    throw new ceylon.language.meta.model.TypeApplicationException("Cannot apply a member declaration with no container type: use memberApply");
        // TODO what is Set is anything other than Nothing?
           // c.f. classApply with an incorect parameters
        com.redhat.ceylon.model.typechecker.model.Value modelDecl = (com.redhat.ceylon.model.typechecker.model.Value)declaration;
        com.redhat.ceylon.model.typechecker.model.TypedReference typedReference = modelDecl.appliedTypedReference(null, Collections.<Type>emptyList());

        com.redhat.ceylon.model.typechecker.model.Type getType = typedReference.getType();
        TypeDescriptor reifiedGet = Metamodel.getTypeDescriptorForProducedType(getType.getQualifyingType());
        // immutable values have Set=Nothing
        com.redhat.ceylon.model.typechecker.model.Type setType = getVariable() ? 
                getType : modelDecl.getUnit().getNothingType();
        TypeDescriptor reifiedSet = getVariable() ? reifiedGet : TypeDescriptor.NothingType;
        
        Metamodel.checkReifiedTypeArgument("apply", "Value<$1,$2>", 
                Variance.OUT, getType, $reifiedGet,
                Variance.IN, setType, $reifiedSet);
        // XXX This is a lie, and we only get away with it due to erasure
        return (ceylon.language.meta.model.Value<Get,Set>)new AppliedValueConstructor<Get>(reifiedGet, this, typedReference, null, null);
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
                @Name("containerType") ceylon.language.meta.model.Type<? extends Object> containerType){
        if(getToplevel())
            throw new ceylon.language.meta.model.TypeApplicationException("Cannot apply a toplevel declaration to a container type: use apply");
        Type qualifyingType = Metamodel.getModel(containerType);
        Metamodel.checkQualifyingType(qualifyingType, declaration);
        com.redhat.ceylon.model.typechecker.model.Value modelDecl = (com.redhat.ceylon.model.typechecker.model.Value)declaration;
        // find the proper qualifying type
        Type memberQualifyingType = qualifyingType.getSupertype((TypeDeclaration) modelDecl.getContainer());
        com.redhat.ceylon.model.typechecker.model.TypedReference typedReference = modelDecl.appliedTypedReference(memberQualifyingType, Collections.<Type>emptyList());
        TypeDescriptor reifiedContainer = Metamodel.getTypeDescriptorForProducedType(qualifyingType);
        
        com.redhat.ceylon.model.typechecker.model.Type getType = typedReference.getType();
        TypeDescriptor reifiedGet = Metamodel.getTypeDescriptorForProducedType(getType);
        // immutable values have Set=Nothing
        com.redhat.ceylon.model.typechecker.model.Type setType = getVariable() ? 
                getType : modelDecl.getUnit().getNothingType();
        TypeDescriptor reifiedSet = getVariable() ? reifiedGet : TypeDescriptor.NothingType;
        
        Metamodel.checkReifiedTypeArgument("memberApply", "Attribute<$1,$2,$3>", 
                Variance.IN, memberQualifyingType, $reifiedContainer,
                Variance.OUT, getType, $reifiedGet,
                Variance.IN, setType, $reifiedSet);
        return (ceylon.language.meta.model.Attribute)new AppliedValueMemberConstructor<Container,Get>(
                reifiedContainer, reifiedGet, this, typedReference, null);
    }
    
    @Override
    public <Type> ValueConstructor<Type> constructorApply(TypeDescriptor $reified$Type) {
        return (ValueConstructor)apply($reified$Type, Nothing.NothingType);
    }
    
    @Override
    public <Container,Type> MemberClassValueConstructor<Container,Type> memberConstructorApply(
            TypeDescriptor $reified$Container, 
            TypeDescriptor $reified$Type,
            @Name("containerType") ceylon.language.meta.model.Type<? extends Object> containerType) {
        return (MemberClassValueConstructor)memberApply($reified$Container, $reified$Type, Nothing.NothingType, containerType);
    }
}
