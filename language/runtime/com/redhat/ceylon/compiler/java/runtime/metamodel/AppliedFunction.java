package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.model.Function$impl;
import ceylon.language.meta.model.FunctionModel$impl;
import ceylon.language.meta.model.Model$impl;

import com.redhat.ceylon.compiler.java.Util;
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
    implements ceylon.language.meta.model.Function<Type, Arguments>, ReifiedType {

    @Ignore
    private final TypeDescriptor $reifiedType;
    @Ignore
    private final TypeDescriptor $reifiedArguments;
    
    private ceylon.language.meta.model.Type<? extends Type> type;
    protected FreeFunction declaration;
    private MethodHandle method;
    private MethodHandle[] dispatch;
    private int firstDefaulted = -1;
    private int variadicIndex = -1;
    private ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> typeArguments;
    private Object instance;
    private ceylon.language.meta.model.Type<? extends java.lang.Object> container;

    public AppliedFunction(@Ignore TypeDescriptor $reifiedType, 
                           @Ignore TypeDescriptor $reifiedArguments,
                           ProducedReference appliedFunction, FreeFunction function, 
                           ceylon.language.meta.model.Type<?> container,
                           Object instance) {
        this.$reifiedType = $reifiedType;
        this.$reifiedArguments = $reifiedArguments;
        this.container = container;
        this.instance = instance;
        
        com.redhat.ceylon.compiler.typechecker.model.Method decl = (com.redhat.ceylon.compiler.typechecker.model.Method) function.declaration;
        List<Parameter> parameters = decl.getParameterLists().get(0).getParameters();
        
        this.firstDefaulted = Metamodel.getFirstDefaultedParameter(parameters);
        this.variadicIndex = Metamodel.getVariadicParameter(parameters);

        Method[] defaultedMethods = null;
        if(firstDefaulted != -1){
            // if we have 2 params and first is defaulted we need 2 + 1 - 0 = 3 methods:
            // f(), f(a) and f(a, b)
            this.dispatch = new MethodHandle[parameters.size() + 1 - firstDefaulted];
            defaultedMethods = new Method[dispatch.length];
        }

        this.type = Metamodel.getAppliedMetamodel(Metamodel.getFunctionReturnType(appliedFunction));
        
        this.declaration = function;
        
        this.typeArguments = Metamodel.getTypeArguments(declaration, appliedFunction);

        // FIXME: delay method setup for when we actually use it?
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(function.declaration);
        Method found = null;
        String name = Metamodel.getJavaMethodName((com.redhat.ceylon.compiler.typechecker.model.Method) function.declaration);
        
        // special cases for some erased types
        if(javaClass == ceylon.language.Object.class
                || javaClass == ceylon.language.Basic.class
                || javaClass == ceylon.language.Identifiable.class){
            if("equals".equals(name)){
                // go fetch the method Object.equals
                try {
                    found = java.lang.Object.class.getDeclaredMethod("equals", java.lang.Object.class);
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                throw new RuntimeException("Object/Basic/Identifiable member not supported: "+decl.getName());
            }
        }else{
            // FIXME: deal with Java classes and overloading
            // FIXME: faster lookup with types? but then we have to deal with erasure and stuff
            found = Metamodel.getJavaMethod((com.redhat.ceylon.compiler.typechecker.model.Method) function.declaration);;
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
        }
        if(found != null){
            boolean variadic = found.isVarArgs();
            method = reflectionToMethodHandle(found, javaClass, instance, appliedFunction, parameters, variadic, false);
            if(defaultedMethods != null){
                // this won't find the last one, but it's method
                int i=0;
                for(;i<defaultedMethods.length-1;i++){
                    // FIXME: proper checks
                    dispatch[i] = reflectionToMethodHandle(defaultedMethods[i], javaClass, instance, appliedFunction, parameters, variadic, false);
                }
                dispatch[i] = method;
            }else if(variadic){
                // variadic methods don't have defaulted parameters, but we will simulate one because our calling convention is that
                // we treat variadic methods as if the last parameter is optional
                firstDefaulted = parameters.size() - 1;
                dispatch = new MethodHandle[2];
                dispatch[0] = reflectionToMethodHandle(found, javaClass, instance, appliedFunction, parameters, variadic, true);
                dispatch[1] = method;
            }
        }
    }

    private MethodHandle reflectionToMethodHandle(Method found, java.lang.Class<?> javaClass, Object instance, 
                                                  ProducedReference appliedFunction, List<Parameter> parameters,
                                                  boolean variadic, boolean bindVariadicParameterToEmptyArray) {
        MethodHandle method;
        try {
            if(!Modifier.isPublic(found.getModifiers()));
                found.setAccessible(true);
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
        method = MethodHandleUtil.unboxArguments(method, skipParameters, 0, parameterTypes,
                                                 parameterProducedTypes, variadic, bindVariadicParameterToEmptyArray);
        return method;
    }

    @Override
    public FreeFunction getDeclaration(){
        return declaration;
    }
    
    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> getTypeArguments() {
        return typeArguments;
    }

    @Override
    @Ignore
    public ceylon.language.meta.model.Generic$impl $ceylon$language$meta$model$Generic$impl() {
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$meta$model$Model$impl() {
        return null;
    }
    
    @Override
    @Ignore
    public FunctionModel$impl<Type, Arguments> $ceylon$language$meta$model$FunctionModel$impl() {
        return null;
    }
    
    @Override
    @Ignore
    public Function$impl<Type, Arguments> $ceylon$language$meta$model$Function$impl() {
        return null;
    }

    @Ignore
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
            Util.rethrow(e);
            return null;
        }
    }

    @Ignore
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
            Util.rethrow(e);
            return null;
        }
    }

    @Ignore
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
            Util.rethrow(e);
            return null;
        }
    }

    @Ignore
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
            Util.rethrow(e);
            return null;
        }
    }

    @Ignore
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
            Util.rethrow(e);
            return null;
        }
    }

    @Ignore
    @Override
    public short $getVariadicParameterIndex() {
        return (short)variadicIndex;
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<Type>")
    public ceylon.language.meta.model.Type<? extends Type> getType() {
        return type;
    }

    @Override
    @Ignore
    public Type $call$variadic() {
        return $call();
    }
    
    @Override
    @Ignore
    public Type $call$variadic(Sequential<?> varargs) {
        return $call(varargs);
    }

    @Override
    @Ignore
    public Type $call$variadic(Object arg0,
            Sequential<?> varargs) {
        return $call(arg0, varargs);
    }

    @Override
    @Ignore
    public Type $call$variadic(Object arg0,
            Object arg1, Sequential<?> varargs) {
        return $call(arg0, arg1, varargs);
    }

    @Override
    @Ignore
    public Type $call$variadic(Object arg0,
            Object arg1, Object arg2, Sequential<?> varargs) {
        return $call(arg0, arg1, arg2, varargs);
    }

    @Override
    @Ignore
    public Type $call$variadic(Object... argsAndVarargs) {
        return $call((Object[])argsAndVarargs);
    }

    @Override
    @Ignore
    public Type $call$variadic(Object arg0) {
        return $call(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Type $call$variadic(Object arg0, Object arg1) {
        return $call(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Type $call$variadic(Object arg0, Object arg1, Object arg2) {
        return $call(arg0, arg1, arg2, empty_.get_());
    }

    @Override
    public int hashCode() {
        int result = 1;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        result = 37 * result + (instance == null ? 0 : instance.hashCode());
        result = 37 * result + getDeclaration().hashCode();
        result = 37 * result + getTypeArguments().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof AppliedFunction == false)
            return false;
        AppliedFunction other = (AppliedFunction) obj;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        return getDeclaration().equals(other.getDeclaration())
                && Util.eq(instance, other.instance)
                && getTypeArguments().equals(other.getTypeArguments());
    }


    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>|ceylon.language::Null")
    public ceylon.language.meta.model.Type<? extends java.lang.Object> getContainer(){
        return container;
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedFunction.class, $reifiedType, $reifiedArguments);
    }
}
