package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.FunctionDeclaration;
import ceylon.language.meta.model.Function;

import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.TypedReference;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class(extendsType="ceylon.language::Basic")
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
})
public class AppliedMethod<Container, Type, Arguments extends Sequential<? extends Object>> 
    extends AppliedMember<Container, ceylon.language.meta.model.Function<? extends Type, ? super Arguments>> 
    implements ceylon.language.meta.model.Method<Container, Type, Arguments> {

    protected final FreeFunction declaration;
    private Reference appliedFunction;
    private ceylon.language.meta.model.Type<Type> closedType;
    @Ignore
    protected TypeDescriptor $reifiedType;
    @Ignore
    protected TypeDescriptor $reifiedArguments;
    
    private Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> typeArguments;
    private Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends Sequence<? extends Object>> typeArgumentWithVariances;
    private Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> parameterTypes;

    public AppliedMethod(@Ignore TypeDescriptor $reifiedContainer, 
                         @Ignore TypeDescriptor $reifiedType, 
                         @Ignore TypeDescriptor $reifiedArguments, 
                         Reference appliedFunction, 
                         FreeFunction declaration,
                         ceylon.language.meta.model.Type<? extends Object> container) {
        super($reifiedContainer, TypeDescriptor.klass(ceylon.language.meta.model.Function.class, $reifiedType, $reifiedArguments), container);
        this.$reifiedType = $reifiedType;
        this.$reifiedArguments = $reifiedArguments;
        this.appliedFunction = appliedFunction;
        this.declaration = declaration;
        this.typeArguments = Metamodel.getTypeArguments(declaration, appliedFunction);
        this.typeArgumentWithVariances = Metamodel.getTypeArgumentWithVariances(declaration, appliedFunction);
        this.closedType = Metamodel.getAppliedMetamodel(Metamodel.getFunctionReturnType(appliedFunction));
        // get a list of produced parameter types
        com.redhat.ceylon.model.typechecker.model.Functional method = (com.redhat.ceylon.model.typechecker.model.Functional)appliedFunction.getDeclaration();
        List<Parameter> parameters = method.getFirstParameterList().getParameters();
        List<com.redhat.ceylon.model.typechecker.model.Type> parameterProducedTypes = Metamodel.getParameterProducedTypes(parameters, appliedFunction);
        this.parameterTypes = Decl.isConstructor(declaration.declaration)  ? null : Metamodel.getAppliedMetamodelSequential(parameterProducedTypes);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,ceylon.language.meta.model::Type<ceylon.language::Anything>>")
    public ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> getTypeArguments() {
        return typeArguments;
    }
    
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>> getTypeArgumentList() {
        return Metamodel.getTypeArgumentList(this);
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,[ceylon.language.meta.model::Type<ceylon.language::Anything>,ceylon.language.meta.declaration::Variance]>")
    public ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.Sequence<? extends Object>> getTypeArgumentWithVariances() {
        return typeArgumentWithVariances;
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<[ceylon.language.meta.model::Type<ceylon.language::Anything>,ceylon.language.meta.declaration::Variance]>")
    public ceylon.language.Sequential<? extends ceylon.language.Sequence<? extends Object>> getTypeArgumentWithVarianceList() {
        return Metamodel.getTypeArgumentWithVarianceList(this);
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::FunctionDeclaration")
    public FunctionDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<Type>")
    public ceylon.language.meta.model.Type<? extends Type> getType() {
        return closedType;
    }

    @Override
    protected Function<Type, Arguments> bindTo(Object instance) {
        return new AppliedFunction<Type, Arguments>($reifiedType, $reifiedArguments, appliedFunction, declaration, getContainer(), instance);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AppliedMethod.class, super.$reifiedContainer, $reifiedType, $reifiedArguments);
    }
    
    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $callvariadic$() {
        return $callvariadic$(empty_.get_());
    }
    
    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $callvariadic$(
            Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $callvariadic$(
            Object... argsAndVarargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0) {
        return $callvariadic$(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1) {
        return $callvariadic$(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Object arg2) {
        return $callvariadic$(arg0, arg1, arg2, empty_.get_());
    }

    @Override
    public Function<? extends Type, ? super Arguments> bind(@TypeInfo("ceylon.language::Object") @Name("container") java.lang.Object container){
        return (Function<? extends Type, ? super Arguments>) Metamodel.bind(this, this.appliedFunction.getQualifyingType(), container);
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes(){
        return parameterTypes;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaringType().hashCode();
        result = 37 * result + getDeclaration().hashCode();
        // so far, functions can't have use-site variance
        result = 37 * result + getTypeArguments().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.model.Method == false)
            return false;
        ceylon.language.meta.model.Method<?, ?, ?> other = (ceylon.language.meta.model.Method<?, ?, ?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getDeclaringType().equals(other.getDeclaringType())
                // so far, functions can't have use-site variance
                && getTypeArguments().equals(other.getTypeArguments());
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.meta.model.Type<?> getContainer(){
        return getDeclaringType();
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }
}
