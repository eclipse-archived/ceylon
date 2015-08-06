package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.Member;
import ceylon.language.meta.model.MemberClass;
import ceylon.language.meta.model.MemberClassValueConstructor;
import ceylon.language.meta.model.ValueConstructor;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor.Nothing;
import com.redhat.ceylon.model.typechecker.model.TypedReference;


@Ceylon(major=8)
@com.redhat.ceylon.compiler.java.metadata.Class
@SatisfiedTypes("ceylon.language.meta.model::MemberClassValueConstructor<Container,Get,Set>")
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Get", variance = Variance.OUT),
    @TypeParameter(value = "Set", variance = Variance.IN)
})
public class AppliedValueMemberConstructor<Container, Get, Set>
        extends AppliedMember<Container, ceylon.language.meta.model.ValueConstructor<? extends Get, ? super Set>> 
        implements MemberClassValueConstructor<Container, Get, Set> {

    final AppliedMemberClass<Container, Get, ?> clazz;
    
    protected final FreeValueConstructor declaration;
    protected final TypedReference typedReference;
    private final ceylon.language.meta.model.Type<? extends Get> closedType;
    @Ignore
    protected final TypeDescriptor $reifiedGet;
    @Ignore
    protected final TypeDescriptor $reifiedSet;
    
    public AppliedValueMemberConstructor(TypeDescriptor $reifiedContainer,
            TypeDescriptor $reifiedGet,
            TypeDescriptor $reifiedSet,
            FreeValueConstructor declaration, TypedReference typedReference,
            AppliedMemberClass<Container, Get, ?> clazz) {
        super($reifiedContainer, TypeDescriptor.klass(ceylon.language.meta.model.Value.class, $reifiedGet, $reifiedSet), clazz.getContainer());
        this.declaration = declaration;
        this.typedReference = typedReference;
        this.closedType = Metamodel.getAppliedMetamodel(typedReference.getType());
        this.$reifiedGet = $reifiedGet;
        this.$reifiedSet = Nothing.NothingType;
        this.clazz = clazz;
    }
    
    @Override
    public MemberClass<Container, Get, ?> getType() {
        return clazz;
    }
    
    @Override
    public ceylon.language.meta.model.ClassModel<Get, ?> getContainer() {
        return (ceylon.language.meta.model.ClassModel)clazz.getContainer();
    }
    
    public ValueConstructorDeclaration getDeclaration() {
        return (ValueConstructorDeclaration)declaration;
    }
    
    @TypeInfo("ceylon.language.meta.model::ValueConstructor<Type,Set>")
    @Override
    public ValueConstructor<Get, Set> bind(Object instance) {
        return null;
    }
    
    
    ////////////////////////////

    
    @Override
    protected ValueConstructor<? extends Get, ? super Set> bindTo(Object instance) {
        return new AppliedValueConstructor<Get,Set>(
                $reifiedGet, $reifiedSet, declaration, typedReference, (AppliedClass)clazz.getContainer(), instance);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AppliedValueMemberConstructor.class, super.$reifiedContainer, $reifiedGet, $reifiedSet);
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get, ? super Set> $callvariadic$() {
        return $callvariadic$(empty_.get_());
    }
    
    @Override
    @Ignore
    public ValueConstructor<? extends Get, ? super Set> $callvariadic$(
            Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get, ? super Set> $callvariadic$(
            Object arg0, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get, ? super Set> $callvariadic$(
            Object arg0, Object arg1, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get, ? super Set> $callvariadic$(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get, ? super Set> $callvariadic$(Object... argsAndVarargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get, ? super Set> $callvariadic$(Object arg0) {
        return $callvariadic$(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get, ? super Set> $callvariadic$(Object arg0, Object arg1) {
        return $callvariadic$(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public ValueConstructor<? extends Get, ? super Set> $callvariadic$(Object arg0, Object arg1,
            Object arg2) {
        return $callvariadic$(arg0, arg1, arg2, empty_.get_());
    }

    //@Override
    //public Value<? extends Get, ? super Set> bind(@TypeInfo("ceylon.language::Object") @Name("container") java.lang.Object container){
    //    return (Value<? extends Get, ? super Set>) Metamodel.bind(this, this.typedReference.getQualifyingType(), container);
    //}

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
        if(obj instanceof ceylon.language.meta.model.MemberClassValueConstructor == false)
            return false;
        ceylon.language.meta.model.MemberClassValueConstructor<?,?,?> other = (ceylon.language.meta.model.MemberClassValueConstructor<?,?,?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getDeclaringType().equals(((Member<?,?>)other).getDeclaringType());
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }
}