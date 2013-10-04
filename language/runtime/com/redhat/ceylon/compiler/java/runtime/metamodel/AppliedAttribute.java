package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.model.Attribute$impl;
import ceylon.language.meta.model.Model$impl;
import ceylon.language.meta.model.Value;
import ceylon.language.meta.model.ValueModel$impl;
import ceylon.language.meta.declaration.ValueDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
})
public class AppliedAttribute<Container, Type> 
    extends AppliedMember<Container, ceylon.language.meta.model.Value<? extends Type>>
    implements ceylon.language.meta.model.Attribute<Container, Type> {

    protected FreeAttribute declaration;
    protected ProducedTypedReference typedReference;
    private ceylon.language.meta.model.Type<? extends Type> closedType;
    @Ignore
    protected final TypeDescriptor $reifiedType;
    
    public AppliedAttribute(@Ignore TypeDescriptor $reifiedContainer, 
                            @Ignore TypeDescriptor $reifiedType,
                            FreeAttribute declaration, ProducedTypedReference typedReference,
                            ceylon.language.meta.model.Type<? extends Object> container) {
        super($reifiedContainer, TypeDescriptor.klass(ceylon.language.meta.model.Value.class, $reifiedType), container);
        this.declaration = declaration;
        this.typedReference = typedReference;
        this.closedType = Metamodel.getAppliedMetamodel(typedReference.getType());
        this.$reifiedType = $reifiedType;
    }

    @Override
    @Ignore
    public ValueModel$impl<Type> $ceylon$language$meta$model$ValueModel$impl() {
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$meta$model$Model$impl() {
        return null;
    }

    @Override
    @Ignore
    public Attribute$impl<Container, Type> $ceylon$language$meta$model$Attribute$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::ValueDeclaration")
    public ValueDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<Type>")
    public ceylon.language.meta.model.Type<? extends Type> getType() {
        return closedType;
    }
    
    @Override
    protected Value<? extends Type> bindTo(Object instance) {
        return new AppliedValue<Type>($reifiedType, declaration, typedReference, getContainer(), instance);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedAttribute.class, super.$reifiedContainer, $reifiedType);
    }

    public static <ContainerType, ValueType> ceylon.language.meta.model.Attribute<ContainerType, ValueType> 
        instance(@Ignore TypeDescriptor $reifiedContainer, @Ignore TypeDescriptor reifiedValueType, 
                 FreeAttribute value, ProducedTypedReference valueTypedReference, 
                 com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration decl,
                 ceylon.language.meta.model.ClassOrInterface<? extends Object> container) {
        return decl.isVariable()
                ? new AppliedVariableAttribute<ContainerType, ValueType>($reifiedContainer, reifiedValueType, value, valueTypedReference, container)
                : new AppliedAttribute<ContainerType, ValueType>($reifiedContainer, reifiedValueType, value, valueTypedReference, container);
    }
    
    @Override
    @Ignore
    public Value<? extends Type> $call$variadic() {
        return $call$variadic(empty_.get_());
    }
    
    @Override
    @Ignore
    public Value<? extends Type> $call$variadic(
            Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Value<? extends Type> $call$variadic(
            Object arg0, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Value<? extends Type> $call$variadic(
            Object arg0, Object arg1, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Value<? extends Type> $call$variadic(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Value<? extends Type> $call$variadic(Object... argsAndVarargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Value<? extends Type> $call$variadic(Object arg0) {
        return $call$variadic(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Value<? extends Type> $call$variadic(Object arg0, Object arg1) {
        return $call$variadic(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Value<? extends Type> $call$variadic(Object arg0, Object arg1,
            Object arg2) {
        return $call$variadic(arg0, arg1, arg2, empty_.get_());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaringType().hashCode();
        result = 37 * result + getDeclaration().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.model.Attribute == false)
            return false;
        ceylon.language.meta.model.Attribute<?,?> other = (ceylon.language.meta.model.Attribute<?,?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getDeclaringType().equals(other.getDeclaringType());
    }


    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.meta.model.Type<? extends java.lang.Object> getContainer(){
        return getDeclaringType();
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }
}
