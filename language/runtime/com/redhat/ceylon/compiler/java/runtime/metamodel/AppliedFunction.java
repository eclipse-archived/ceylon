package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ceylon.language.Sequential;
import ceylon.language.metamodel.AppliedType;
import ceylon.language.metamodel.AppliedType$impl;
import ceylon.language.metamodel.Declaration$impl;
import ceylon.language.metamodel.Function$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.model.JavaMethod;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })
public class AppliedFunction<Type, Arguments extends Sequential<? extends Object>> 
    implements ceylon.language.metamodel.Function<Type, Arguments>, ReifiedType {

    @Ignore
    private final TypeDescriptor $reifiedType;
    @Ignore
    private final TypeDescriptor $reifiedArguments;
    
    private AppliedType type;
    private FreeFunction declaration;
    private MethodHandle method;

    public AppliedFunction(ProducedReference appliedFunction, FreeFunction function, Object instance) {
        ProducedType appliedType = appliedFunction.getType();
        
        // FIXME: check that this returns a Callable if we have multiple parameter lists
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(appliedType);

        com.redhat.ceylon.compiler.typechecker.model.Method decl = (com.redhat.ceylon.compiler.typechecker.model.Method) function.declaration;
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> elemTypes = new LinkedList<com.redhat.ceylon.compiler.typechecker.model.ProducedType>();
        for(Parameter param : decl.getParameterLists().get(0).getParameters()){
            com.redhat.ceylon.compiler.typechecker.model.ProducedType paramType = param.getType().substitute(appliedFunction.getTypeArguments());
            elemTypes.add(paramType);
        }
        // FIXME: last three params, copy/share from ExpressionVisitor.getParameterTypesAsTupleType()
        com.redhat.ceylon.compiler.typechecker.model.ProducedType tupleType = decl.getUnit().getTupleType(elemTypes, false, false, -1);
        this.$reifiedArguments = Metamodel.getTypeDescriptorForProducedType(tupleType);

        this.type = Metamodel.getAppliedMetamodel(appliedType);
        
        this.declaration = function;

        // FIXME: delay method setup for when we actually use it?
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(function.declaration);
        // FIXME: deal with Java classes and overloading
        // FIXME: faster lookup with types? but then we have to deal with erasure and stuff
        // FIXME: perhaps we should just store the damn method in the underlying JavaMethod?
        Method found = null;
        String name;
        // FIXME: introduce a damn interface for getRealName()
        if(function.declaration instanceof JavaMethod)
            name = ((JavaMethod)function.declaration).getRealName();
        else if(function.declaration instanceof LazyMethod){
            name = ((LazyMethod)function.declaration).getRealMethodName();
        }else
            throw new RuntimeException("Function declaration type not supported yet: "+function.declaration);
        for(Method method : javaClass.getDeclaredMethods()){
            if(method.isAnnotationPresent(Ignore.class))
                continue;
            if(!method.getName().equals(name))
                continue;
            // FIXME: deal with private stuff?
            if(found != null){
                throw new RuntimeException("More than one method found for: "+javaClass+", 1st: "+found+", 2nd: "+method);
            }
            found = method;
        }
        if(found != null){
            try {
                method = MethodHandles.lookup().unreflect(found);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Problem getting a MH for constructor for: "+javaClass, e);
            }
            // box the return type
            method = MethodHandleUtil.boxReturnValue(method, found.getReturnType());
            // we need to cast to Object because this is what comes out when calling it in $call
            java.lang.Class<?>[] parameterTypes = found.getParameterTypes();
            if(instance != null)
                method = method.bindTo(instance);
            method = method.asType(MethodType.methodType(Object.class, parameterTypes));
            int typeParametersCount = found.getTypeParameters().length;
            // insert any required type descriptors
            // FIXME: only if it's expecting them!
            if(typeParametersCount != 0){
                List<ProducedType> typeArguments = new ArrayList<ProducedType>();
                Map<com.redhat.ceylon.compiler.typechecker.model.TypeParameter, ProducedType> typeArgumentMap = appliedFunction.getTypeArguments();
                for (com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : ((com.redhat.ceylon.compiler.typechecker.model.Method)appliedFunction.getDeclaration()).getTypeParameters()) {
                    typeArguments.add(typeArgumentMap.get(tp));
                }
                method = MethodHandleUtil.insertReifiedTypeArguments(method, 0, typeArguments);
            }
            // now convert all arguments (we may need to unbox)
            method = MethodHandleUtil.unboxArguments(method, typeParametersCount, 0, parameterTypes);
        }
    }

    @Override
    public FreeFunction getDeclaration(){
        return declaration;
    }
    
    @Override
    @Ignore
    public Declaration$impl $ceylon$language$metamodel$Declaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Function$impl<Type, Arguments> $ceylon$language$metamodel$Function$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call() {
        if(method == null)
            throw new RuntimeException("No method found for: "+declaration.getName());
        try {
            return (Type)method.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke method for "+declaration.getName(), e);
        }
    }

    @Override
    public Type $call(Object arg0) {
        if(method == null)
            throw new RuntimeException("No method found for: "+declaration.getName());
        try {
            return (Type)method.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke method for "+declaration.getName(), e);
        }
    }

    @Override
    public Type $call(Object arg0, Object arg1) {
        if(method == null)
            throw new RuntimeException("No method found for: "+declaration.getName());
        try {
            return (Type)method.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke method for "+declaration.getName(), e);
        }
    }

    @Override
    public Type $call(Object arg0, Object arg1, Object arg2) {
        if(method == null)
            throw new RuntimeException("No method found for: "+declaration.getName());
        try {
            return (Type)method.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke method for "+declaration.getName(), e);
        }
    }

    @Override
    public Type $call(Object... args) {
        if(method == null)
            throw new RuntimeException("No method found for: "+declaration.getName());
        try {
            // FIXME: this does not do invokeExact and does boxing/widening
            return (Type)method.invokeWithArguments(args);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke method for "+declaration.getName(), e);
        }
    }

    @Override
    public short $getVariadicParameterIndex() {
        // TODO Auto-generated method stub
        return -1;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::AppliedType")
    public AppliedType getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedFunction.class, $reifiedType, $reifiedArguments);
    }
}
