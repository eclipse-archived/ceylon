package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.model.Class$impl;
import ceylon.language.meta.model.ClassModel$impl;
import ceylon.language.meta.model.InvocationException;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })
public class AppliedClass<Type, Arguments extends Sequential<? extends Object>> 
    extends AppliedClassOrInterface<Type>
    implements ceylon.language.meta.model.Class<Type, Arguments> {

    final TypeDescriptor $reifiedArguments;
    private MethodHandle constructor;
    private Object instance;
    private int firstDefaulted;
    private int variadicIndex = -1;
    private MethodHandle[] dispatch;
    private ceylon.language.meta.model.Type<?> container;
    
    // FIXME: get rid of duplicate instantiations of AppliedClassType when the type in question has no type parameters
    public AppliedClass(@Ignore TypeDescriptor $reifiedType, 
                        @Ignore TypeDescriptor $reifiedArguments,
                        com.redhat.ceylon.compiler.typechecker.model.ProducedType producedType, 
                        ceylon.language.meta.model.Type<?> container, Object instance) {
        super($reifiedType, producedType);
        this.$reifiedArguments = $reifiedArguments;
        this.container = container;
        this.instance = instance;
    }

    @Override
    @Ignore
    public Class$impl<Type, Arguments> $ceylon$language$meta$model$Class$impl() {
        return null;
    }

    @Override
    @Ignore
    public ClassModel$impl<Type, Arguments> $ceylon$language$meta$model$ClassModel$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::ClassDeclaration")
    public ceylon.language.meta.declaration.ClassDeclaration getDeclaration() {
        return (ceylon.language.meta.declaration.ClassDeclaration) super.getDeclaration();
    }

    @Override
    protected void init() {
        super.init();
        com.redhat.ceylon.compiler.typechecker.model.Class decl = (com.redhat.ceylon.compiler.typechecker.model.Class) producedType.getDeclaration();

        // anonymous classes don't have constructors
        // local classes have constructors but if they capture anything it will get extra parameters that nobody knows about
        // FIXME: so we really want to disallow that in the metamodel?
        if(!decl.isAnonymous() && !Metamodel.isLocalType(decl)){
            initConstructor(decl);
        }
    }

    private void initConstructor(com.redhat.ceylon.compiler.typechecker.model.Class decl) {
        List<Parameter> parameters = decl.getParameterLists().get(0).getParameters();
        this.firstDefaulted = Metamodel.getFirstDefaultedParameter(parameters);
        this.variadicIndex = Metamodel.getVariadicParameter(parameters);

        Object[] defaultedMethods = null;
        if(firstDefaulted != -1){
            // if we have 2 params and first is defaulted we need 2 + 1 - 0 = 3 methods:
            // f(), f(a) and f(a, b)
            this.dispatch = new MethodHandle[parameters.size() + 1 - firstDefaulted];
            defaultedMethods = new Object[dispatch.length];
        }

        // FIXME: delay constructor setup for when we actually use it?
        // FIXME: finding the right MethodHandle for the constructor could actually be done in the Class declaration
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(declaration.declaration);
        // FIXME: faster lookup with types? but then we have to deal with erasure and stuff
        Object found = null;
        if(!javaClass.isMemberClass() 
                || !Metamodel.isCeylon(decl)
                // private ceylon member classes don't have any outer constructor method so treat them like java members
                || !decl.isShared()){
            for(Constructor<?> constr : javaClass.getDeclaredConstructors()){
                if(constr.isAnnotationPresent(Ignore.class)){
                    // it's likely an overloaded constructor
                    // FIXME: proper checks
                    if(firstDefaulted != -1){
                        // this doesn't need to count synthetic parameters because we only use the constructor for Java types
                        // which can't have defaulted parameters
                        int params = constr.getParameterTypes().length;
                        defaultedMethods[params - firstDefaulted] = constr;
                    }
                    continue;
                }
                // FIXME: deal with private stuff?
                if(found != null){
                    throw new RuntimeException("More than one constructor found for: "+javaClass+", 1st: "+found+", 2nd: "+constr);
                }
                found = constr;
            }
        }else{
            String builderName = declaration.getName() + "$new";
            // FIXME: this probably doesn't work for local classes
            // FIXME: perhaps store and access the container class literal from an extra param of @Container?
            java.lang.Class<?> outerJavaClass = Metamodel.getJavaClass((Declaration) declaration.declaration.getContainer());
            for(Method meth : outerJavaClass.getDeclaredMethods()){
                // FIXME: we need a better way to look things up: they're all @Ignore...
//                if(meth.isAnnotationPresent(Ignore.class))
//                    continue;
                if(!meth.getName().equals(builderName))
                    continue;
                // FIXME: proper checks
                if(firstDefaulted != -1){
                    int params = meth.getParameterTypes().length;
                    if(params != parameters.size()){
                        defaultedMethods[params - firstDefaulted] = meth;
                        continue;
                    }
                }

                // FIXME: deal with private stuff?
                if(found != null){
                    throw new RuntimeException("More than one constructor method found for: "+javaClass+", 1st: "+found+", 2nd: "+meth);
                }
                found = meth;
            }
        }
        if(found != null){
            boolean variadic = MethodHandleUtil.isVariadicMethodOrConstructor(found);
            constructor = reflectionToMethodHandle(found, javaClass, instance, producedType, parameters, variadic, false);
            if(defaultedMethods != null){
                // this won't find the last one, but it's method
                int i=0;
                for(;i<defaultedMethods.length-1;i++){
                    // FIXME: proper checks
                    dispatch[i] = reflectionToMethodHandle(defaultedMethods[i], javaClass, instance, producedType, parameters, variadic, false);
                }
                dispatch[i] = constructor;
            }else if(variadic){
                // variadic methods don't have defaulted parameters, but we will simulate one because our calling convention is that
                // we treat variadic methods as if the last parameter is optional
                firstDefaulted = parameters.size() - 1;
                dispatch = new MethodHandle[2];
                dispatch[0] = reflectionToMethodHandle(found, javaClass, instance, producedType, parameters, variadic, true);
                dispatch[1] = constructor;
            }
        }
    }

    private MethodHandle reflectionToMethodHandle(Object found, Class<?> javaClass, Object instance2, 
                                                  ProducedType producedType, List<Parameter> parameters,
                                                  boolean variadic, boolean bindVariadicParameterToEmptyArray) {
        MethodHandle constructor = null;
        java.lang.Class<?>[] parameterTypes;
        try {
            if(found instanceof java.lang.reflect.Constructor){
                ((java.lang.reflect.Constructor<?>) found).setAccessible(true);
                constructor = MethodHandles.lookup().unreflectConstructor((java.lang.reflect.Constructor<?>)found);
                parameterTypes = ((java.lang.reflect.Constructor<?>)found).getParameterTypes();
            }else{
                ((Method)found).setAccessible(true);
                constructor = MethodHandles.lookup().unreflect((Method) found);
                parameterTypes = ((java.lang.reflect.Method)found).getParameterTypes();
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Problem getting a MH for constructor for: "+javaClass, e);
        }
        boolean isJavaMember = found instanceof java.lang.reflect.Constructor && instance != null;
        // we need to cast to Object because this is what comes out when calling it in $call
        
        // if it's a java member we will be using the member constructor which has an extra synthetic parameter so we can't bind it
        // until we have casted it first
        if(isJavaMember)
            constructor = constructor.asType(MethodType.methodType(Object.class, parameterTypes));
        // now bind it to the object
        if(instance != null)
            constructor = constructor.bindTo(instance);
        // if it was not a java member we have no extra synthetic instance parameter and we need to get rid of it before casting
        if(!isJavaMember)
            constructor = constructor.asType(MethodType.methodType(Object.class, parameterTypes));
        
        int typeParametersCount = javaClass.getTypeParameters().length;
        int skipParameters = 0;
        if(isJavaMember)
            skipParameters++; // skip the first parameter for boxing
        // insert any required type descriptors
        if(typeParametersCount != 0 && MethodHandleUtil.isReifiedTypeSupported(found, isJavaMember)){
            List<ProducedType> typeArguments = producedType.getTypeArgumentList();
            constructor = MethodHandleUtil.insertReifiedTypeArguments(constructor, 0, typeArguments);
            skipParameters += typeParametersCount;
        }
        // get a list of produced parameter types
        List<ProducedType> parameterProducedTypes = Metamodel.getParameterProducedTypes(parameters, producedType);
        // now convert all arguments (we may need to unbox)
        constructor = MethodHandleUtil.unboxArguments(constructor, skipParameters, 0, parameterTypes,
                                                      parameterProducedTypes, variadic, bindVariadicParameterToEmptyArray);
        
        return constructor;
    }

    @Ignore
    @Override
    public Type $call() {
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                return (Type)constructor.invokeExact();
            // FIXME: proper checks
            return (Type)dispatch[0].invokeExact();
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    private void checkConstructor() {
        if(((FreeClass)declaration).getAbstract())
            throw new InvocationException("Abstract class cannot be instantiated");
        if(constructor == null)
            throw new RuntimeException("No constructor found for: "+declaration.getName());
    }

    @Ignore
    @Override
    public Type $call(Object arg0) {
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                return (Type)constructor.invokeExact(arg0);
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
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                return (Type)constructor.invokeExact(arg0, arg1);
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
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                return (Type)constructor.invokeExact(arg0, arg1, arg2);
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
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                // FIXME: this does not do invokeExact and does boxing/widening
                return (Type)constructor.invokeWithArguments(args);
            // FIXME: proper checks
            return (Type)dispatch[args.length-firstDefaulted].invokeWithArguments(args);
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
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

    @Ignore
    @Override
    public short $getVariadicParameterIndex() {
        checkInit();
        return (short)variadicIndex;
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
        if(obj instanceof AppliedClass == false)
            return false;
        AppliedClass<?,?> other = (AppliedClass<?,?>) obj;
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

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedClass.class, $reifiedType, $reifiedArguments);
    }
}
