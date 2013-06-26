package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ceylon.language.Sequential;
import ceylon.language.metamodel.DeclarationType$impl;
import ceylon.language.metamodel.Function$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
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
    
    private ceylon.language.metamodel.Type type;
    protected FreeFunction declaration;
    private MethodHandle method;
    private MethodHandle[] dispatch;
    private int firstDefaulted = -1;

    public AppliedFunction(@Ignore TypeDescriptor $reifiedType, 
                           @Ignore TypeDescriptor $reifiedArguments,
                           ProducedReference appliedFunction, FreeFunction function, Object instance) {
        ProducedType appliedType = appliedFunction.getType();
        
        // FIXME: check that this returns a Callable if we have multiple parameter lists
        this.$reifiedType = Metamodel.getTypeDescriptorForProducedType(appliedType);

        com.redhat.ceylon.compiler.typechecker.model.Method decl = (com.redhat.ceylon.compiler.typechecker.model.Method) function.declaration;
        List<Parameter> parameters = decl.getParameterLists().get(0).getParameters();
        com.redhat.ceylon.compiler.typechecker.model.ProducedType tupleType 
            = com.redhat.ceylon.compiler.typechecker.analyzer.Util.getParameterTypesAsTupleType(decl.getUnit(), parameters, appliedFunction);
        
        this.firstDefaulted = Metamodel.getFirstDefaultedParameter(parameters);

        Method[] defaultedMethods = null;
        if(firstDefaulted != -1){
            // if we have 2 params and first is defaulted we need 2 + 1 - 0 = 3 methods:
            // f(), f(a) and f(a, b)
            this.dispatch = new MethodHandle[parameters.size() + 1 - firstDefaulted];
            defaultedMethods = new Method[dispatch.length];
        }
        this.$reifiedArguments = Metamodel.getTypeDescriptorForProducedType(tupleType);

        this.type = Metamodel.getAppliedMetamodel(appliedType);
        
        this.declaration = function;

        // FIXME: delay method setup for when we actually use it?
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(function.declaration);
        // FIXME: deal with Java classes and overloading
        // FIXME: faster lookup with types? but then we have to deal with erasure and stuff
        Method found = Metamodel.getJavaMethod((com.redhat.ceylon.compiler.typechecker.model.Method) function.declaration);;
        String name = Metamodel.getJavaMethodName((com.redhat.ceylon.compiler.typechecker.model.Method) function.declaration);
        for(Method method : javaClass.getDeclaredMethods()){
            if(!method.getName().equals(name))
                continue;
            if(method.isAnnotationPresent(Ignore.class)){
                // save method for later
                // FIXME: proper checks
                if(firstDefaulted != -1){
                    int params = method.getParameterTypes().length;
                    defaultedMethods[params - firstDefaulted] = method;
                }
                continue;
            }
            // FIXME: deal with private stuff?
        }
        if(found != null){
            method = reflectionToMethodHandle(found, javaClass, instance, appliedFunction, parameters);
            if(defaultedMethods != null){
                // this won't find the last one, but it's method
                int i=0;
                for(;i<defaultedMethods.length-1;i++){
                    // FIXME: proper checks
                    dispatch[i] = reflectionToMethodHandle(defaultedMethods[i], javaClass, instance, appliedFunction, parameters);
                }
                dispatch[i] = method;
            }
        }
    }

    private MethodHandle reflectionToMethodHandle(Method found, java.lang.Class<?> javaClass, Object instance, ProducedReference appliedFunction, List<Parameter> parameters) {
        MethodHandle method;
        try {
            method = MethodHandles.lookup().unreflect(found);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Problem getting a MH for constructor for: "+javaClass, e);
        }
        // box the return type
        method = MethodHandleUtil.boxReturnValue(method, found.getReturnType(), appliedFunction.getType());
        // we need to cast to Object because this is what comes out when calling it in $call
        java.lang.Class<?>[] parameterTypes = found.getParameterTypes();
        if(instance != null)
            method = method.bindTo(instance);
        method = method.asType(MethodType.methodType(Object.class, parameterTypes));
        int typeParametersCount = found.getTypeParameters().length;
        int skipParameters = 0;
        // insert any required type descriptors
        if(typeParametersCount != 0 && MethodHandleUtil.isReifiedTypeSupported(found, false)){
            List<ProducedType> typeArguments = new ArrayList<ProducedType>();
            Map<com.redhat.ceylon.compiler.typechecker.model.TypeParameter, ProducedType> typeArgumentMap = appliedFunction.getTypeArguments();
            for (com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : ((com.redhat.ceylon.compiler.typechecker.model.Method)appliedFunction.getDeclaration()).getTypeParameters()) {
                typeArguments.add(typeArgumentMap.get(tp));
            }
            method = MethodHandleUtil.insertReifiedTypeArguments(method, 0, typeArguments);
            skipParameters = typeParametersCount;
        }
        // get a list of produced parameter types
        List<ProducedType> parameterProducedTypes = Metamodel.getParameterProducedTypes(parameters, appliedFunction);
        // now convert all arguments (we may need to unbox)
        method = MethodHandleUtil.unboxArguments(method, skipParameters, 0, parameterTypes, parameterProducedTypes, found.isVarArgs());
        return method;
    }

    @Override
    public FreeFunction getDeclaration(){
        return declaration;
    }
    
    @Override
    @Ignore
    public DeclarationType$impl $ceylon$language$metamodel$DeclarationType$impl() {
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
            if(firstDefaulted == -1)
                return (Type)method.invokeExact();
            // FIXME: proper checks
            return (Type)dispatch[0].invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke method for "+declaration.getName(), e);
        }
    }

    @Override
    public Type $call(Object arg0) {
        if(method == null)
            throw new RuntimeException("No method found for: "+declaration.getName());
        try {
            if(firstDefaulted == -1)
                return (Type)method.invokeExact(arg0);
            // FIXME: proper checks
            return (Type)dispatch[1-firstDefaulted].invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke method for "+declaration.getName(), e);
        }
    }

    @Override
    public Type $call(Object arg0, Object arg1) {
        if(method == null)
            throw new RuntimeException("No method found for: "+declaration.getName());
        try {
            if(firstDefaulted == -1)
                return (Type)method.invokeExact(arg0, arg1);
            // FIXME: proper checks
            return (Type)dispatch[2-firstDefaulted].invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke method for "+declaration.getName(), e);
        }
    }

    @Override
    public Type $call(Object arg0, Object arg1, Object arg2) {
        if(method == null)
            throw new RuntimeException("No method found for: "+declaration.getName());
        try {
            if(firstDefaulted == -1)
                return (Type)method.invokeExact(arg0, arg1, arg2);
            // FIXME: proper checks
            return (Type)dispatch[3-firstDefaulted].invokeExact(arg0, arg1, arg2);
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
            if(firstDefaulted == -1)
                return (Type)method.invokeWithArguments(args);
            // FIXME: proper checks
            return (Type)dispatch[args.length-firstDefaulted].invokeWithArguments(args);
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
    @TypeInfo("ceylon.language.metamodel::Type")
    public ceylon.language.metamodel.Type getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedFunction.class, $reifiedType, $reifiedArguments);
    }
}
