package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Array;
import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.model.CallableConstructor;
import ceylon.language.meta.model.ClassModel;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Reference;

@Ceylon(major=8)
@com.redhat.ceylon.compiler.java.metadata.Class
@SatisfiedTypes("ceylon.language.meta.model::CallableConstructor<Type,Arguments>")
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN)
})
public class AppliedCallableConstructor<Type, Arguments extends Sequential<? extends Object>> 
        implements CallableConstructor<Type, Arguments>, ReifiedType, DefaultValueProvider {

    private final TypeDescriptor $reified$Type;
    private final TypeDescriptor $reified$Arguments;
    
    private final FreeClass freeClass;
    final ClassModel<Type,?> appliedClass;
    private final FreeCallableConstructor freeConstructor;
    private final Reference constructorReference;
    private java.lang.Object instance;
    private volatile boolean initialised = false;
    
    private ConstructorDispatch<Type, Arguments> dispatch;
    
    
    public AppliedCallableConstructor(TypeDescriptor $reified$Type,
            TypeDescriptor $reified$Arguments, Reference appliedFunction,
            FreeCallableConstructor freeConstructor,
            ClassModel<Type,?> clazz, Object instance) {
        this.$reified$Type = $reified$Type;
        this.$reified$Arguments = $reified$Arguments;
        this.freeClass = (FreeClass)clazz.getDeclaration();
        this.appliedClass = clazz;
        this.freeConstructor = freeConstructor;
        this.constructorReference = appliedFunction;
        this.instance = instance;
    }
    
    protected void checkInit(){
        if(!initialised){
            synchronized(Metamodel.getLock()){
                if(!initialised){
                    dispatch = new ConstructorDispatch<Type, Arguments>(
                            constructorReference,
                            appliedClass,
                            freeConstructor,
                            ((com.redhat.ceylon.model.typechecker.model.Functional)freeConstructor.declaration).getFirstParameterList().getParameters(),
                            this.instance);
                    initialised = true;
                }
            }
        }
    }
    
    
    
    @Override
    public ClassModel<Type,?> getType() {
        return appliedClass;
    }
    
    @Override
    public ClassModel<Type,?> getContainer() {
        return null;
    }
    
    @Override
    public CallableConstructorDeclaration getDeclaration() {
        return (CallableConstructorDeclaration)freeConstructor;
    }
    
    @Override
    @Ignore
    public Type apply() {
        return apply(empty_.get_());
    }
    
    @Override
    public Type apply(@Name("arguments")
        @Sequenced
        @TypeInfo("ceylon.language::Sequential<ceylon.language::Anything>")
        Sequential<?> arguments){
        checkInit();
        
        ceylon.language.meta.model.Constructor<Type, Sequential<? extends Object>> ctor = dispatch.checkConstructor();
        if (ctor != null) {
            return ctor.apply(arguments);
        } else {
            return Metamodel.apply(this, arguments, 
                    dispatch.parameterProducedTypes, 
                    dispatch.firstDefaulted, 
                    dispatch.variadicIndex);
        }
    }

    @Override
    public Type namedApply(@Name("arguments")
        @TypeInfo("ceylon.language::Iterable<ceylon.language::Entry<ceylon.language::String,ceylon.language::Anything>,ceylon.language::Null>")
        ceylon.language.Iterable<? extends ceylon.language.Entry<? extends ceylon.language.String,? extends java.lang.Object>,? extends java.lang.Object> arguments){
        checkInit();
        ceylon.language.meta.model.Constructor<Type, Sequential<? extends Object>> ctor = dispatch.checkConstructor();
        if (ctor != null) {
            return ctor.namedApply(arguments);
        } else {
            return Metamodel.namedApply(this, this, 
                    (com.redhat.ceylon.model.typechecker.model.Functional)(freeConstructor != null ? freeConstructor.declaration : freeClass.declaration), 
                    arguments, dispatch.getProducedParameterTypes());
        }
    }
    
    @Override
    public Object getDefaultParameterValue(Parameter parameter, Array<Object> values, int collectedValueCount) {
        checkInit();
        return dispatch.getDefaultParameterValue(parameter, values, collectedValueCount);
    }
    
    @Override
    public Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getTypeArgumentList() {
        return appliedClass.getTypeArgumentList();
    }
    
    @Override
    public Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<? extends Object>> getTypeArguments() {
        return appliedClass.getTypeArguments();
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes(){
        checkInit();
        return dispatch.getParameterTypes();
    }
    
    @Override
    public Type $call$() {
        checkInit();
        return dispatch.$call$();
    }
    
    @Override
    public Type $callvariadic$() {
        return $callvariadic$(empty_.get_());
    }
    
    @Override
    public Type $callvariadic$(Sequential<?> varargs) {
        return $call$(varargs);
    }
    
    @Override
    public Type $call$(Object arg0) {
        checkInit();
        return dispatch.$call$(arg0);
    }
    
    @Override
    public Type $callvariadic$(Object arg0) {
        return $callvariadic$(arg0, empty_.get_());
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Sequential<?> varargs) {
        return $call$(arg0, varargs);
    }
    
    @Override
    public Type $call$(Object arg0, Object arg1) {
        checkInit();
        return dispatch.$call$(arg0, arg1);
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Object arg1) {
        return $callvariadic$(arg0, arg1, empty_.get_());
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Sequential<?> varargs) {
        return $call$(arg0, arg1, varargs);
    }
    
    @Override
    public Type $call$(Object arg0, Object arg1, Object arg2) {
        checkInit();
        return dispatch.$call$(arg0, arg1, arg2);
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2) {
        return $callvariadic$(arg0, arg1, arg2, empty_.get_());
    }
    
    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2,
            Sequential<?> varargs) {
        return $call$(arg0, arg1, arg2, varargs);
    }
    
    @Override
    public Type $call$(Object... args) {
        checkInit();
        return dispatch.$call$(args);
    }
    
    @Override
    public Type $callvariadic$(Object... argsAndVarargs) {
        return $call$(argsAndVarargs);
    }
    
    @Override
    public short $getVariadicParameterIndex$() {
        checkInit();
        return dispatch.$getVariadicParameterIndex$();
    }
    
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AppliedCallableConstructor.class, $reified$Type, $reified$Arguments);
    }
    
    public String toString() {
        return appliedClass.toString() + "." + freeConstructor.getName();
    }
    
    @Override
    public int hashCode() {
        return appliedClass.hashCode() ^ freeConstructor.getName().hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof AppliedCallableConstructor) {
            AppliedCallableConstructor autre = (AppliedCallableConstructor)other;
            return this.appliedClass.equals(autre.appliedClass)
                    && freeConstructor.equals(autre.freeConstructor);
        } else {
            return false;
        }
    }
}
