package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import ceylon.language.Array;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.ConstructorDeclaration;
import ceylon.language.meta.model.Class;
import ceylon.language.meta.model.Constructor;
import ceylon.language.meta.model.InvocationException;

import com.redhat.ceylon.compiler.java.Util;
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

@Ceylon(major = 8, minor=0)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(variance=Variance.OUT, value="Type", defaultValue="ceylon.language::Anything"),
    @TypeParameter(variance=Variance.IN, value="Arguments", defaultValue="ceylon.language::Nothing", satisfies="ceylon.language::Sequential<ceylon.language::Anything>")
    })
@SatisfiedTypes("ceylon.language.meta.model::Constructor<Type,Arguments>")
public class AppliedConstructor<Type, Arguments extends Sequential<? extends Object>>
        implements Constructor<Type, Arguments>,
                DefaultValueProvider,
                ReifiedType {
    
    private final TypeDescriptor reified$Type;
    private final TypeDescriptor reified$Arguments;
    protected final FreeConstructor declaration;
    private final List<com.redhat.ceylon.model.typechecker.model.Type> parameterProducedTypes;
    private final Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> parameterTypes;
    private final Class<Type, ? extends Object> container;
    
    private volatile boolean initialized = false;
    
    private int firstDefaulted;
    private int variadicIndex;
    private MethodHandle ctor;
    private MethodHandle[] dispatch;
    private Object instance;
    private com.redhat.ceylon.model.typechecker.model.Type producedType;
    
    public AppliedConstructor(
            @Ignore TypeDescriptor reified$Type,
            @Ignore TypeDescriptor reified$Arguments,
            Class<Type, ? extends Object> container,
            com.redhat.ceylon.model.typechecker.model.Type appliedFunction,
            FreeConstructor declaration,
            Object instance) {
        this.reified$Type = reified$Type;
        this.reified$Arguments = reified$Arguments;
        this.producedType = appliedFunction;
        this.container = container;
        this.declaration = declaration;
        this.instance = instance;
        
        com.redhat.ceylon.model.typechecker.model.Constructor decl = declaration.constructor;
        List<Parameter> parameters = decl.getFirstParameterList().getParameters();
        
        // get a list of produced parameter types
        this.parameterProducedTypes = Metamodel.getParameterProducedTypes(parameters, appliedFunction);
        this.parameterTypes = Metamodel.getAppliedMetamodelSequential(this.parameterProducedTypes);
    }
    
    protected void checkInit() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    init();
                    initialized = true;
                }
            }
        }
    }
    
    private void init() {
        initConstructor();
    }

    private void initConstructor() {
        com.redhat.ceylon.model.typechecker.model.Constructor constructorModel = this.declaration.constructor;
        
        com.redhat.ceylon.model.typechecker.model.Class classModel = (com.redhat.ceylon.model.typechecker.model.Class)constructorModel.getContainer();
        
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(classModel);

        boolean useInstantiator = javaClass.isMemberClass() 
                && Metamodel.isCeylon(classModel)
                // private ceylon member classes don't have any outer constructor method so treat them like java members
                && constructorModel.isShared()
                && classModel.isShared();
        boolean invokeOnCompanionInstance = this.instance != null 
                && classModel.getContainer() instanceof com.redhat.ceylon.model.typechecker.model.Interface
                && !(constructorModel.isShared() && classModel.isShared());
        if (invokeOnCompanionInstance) {
            this.instance = Metamodel.getCompanionInstance(this.instance, (com.redhat.ceylon.model.typechecker.model.Interface)classModel.getContainer());
        }
        
        List<Parameter> parameters = constructorModel.getFirstParameterList().getParameters();
        this.firstDefaulted = Metamodel.getFirstDefaultedParameter(parameters);
        this.variadicIndex = Metamodel.getVariadicParameter(parameters);
        Object found = null;
        Object[] defaultedMethods = null;
        if(firstDefaulted != -1){
            // if we have 2 params and first is defaulted we need 2 + 1 - 0 = 3 methods:
            // f(), f(a) and f(a, b)
            this.dispatch = new MethodHandle[parameters.size() + 1 - firstDefaulted];
            defaultedMethods = new Object[dispatch.length];
        }
        if(!useInstantiator) {
            for(java.lang.reflect.Constructor<?> constr : Metamodel.getJavaConstructors(constructorModel)){
                if(constr.isAnnotationPresent(Ignore.class)){
                    // it's likely an overloaded constructor
                    // FIXME: proper checks
                    if(firstDefaulted != -1){
                        int implicitParameterCount = 0;
                        if (MethodHandleUtil.isReifiedTypeSupported(constr, javaClass.isMemberClass())) { 
                            implicitParameterCount += classModel.getTypeParameters().size();
                        }
                        if (classModel.isClassMember() && javaClass.isMemberClass() 
                                || classModel.isInterfaceMember() && invokeOnCompanionInstance/*!declaration.constructor.isShared()*/) { 
                            // non-shared member classes don't get instantiators, so there's the 
                            // synthetic outerthis parameter to account for.
                            implicitParameterCount++;
                        }
                        if (!Metamodel.isDefaultConstructor(constructorModel)) {
                            // constructor name parameter
                            implicitParameterCount++;
                        }
                        int params = constr.getParameterTypes().length - implicitParameterCount;
                        defaultedMethods[params - firstDefaulted] = constr;
                    }
                    continue;
                }
                // FIXME: deal with private stuff?
                if(found != null){
                    throw Metamodel.newModelError("More than one constructor found for: "+javaClass+", 1st: "+found+", 2nd: "+constr);
                }
                found = constr;
            }
        }else{
            java.lang.String builderName = classModel.getName() + "$new$";
            // FIXME: this probably doesn't work for local classes
            // FIXME: perhaps store and access the container class literal from an extra param of @Container?
            for(Method meth : Metamodel.getJavaInstantiators(constructorModel)){
                // FIXME: we need a better way to look things up: they're all @Ignore...
//                if(meth.isAnnotationPresent(Ignore.class))
//                    continue;
                if(!meth.getName().equals(builderName))
                    continue;
                // FIXME: proper checks
                if(firstDefaulted != -1){
                    int implicitParameterCount = 0;
                    if (MethodHandleUtil.isReifiedTypeSupported(meth, true)) {
                        implicitParameterCount += classModel.getTypeParameters().size();
                    }
                    if (classModel.isInterfaceMember() && !declaration.constructor.isShared()) { 
                        // non-shared member classes don't get instantiators, so there's the 
                        // synthetic outerthis parameter to account for.
                        implicitParameterCount++;
                    }
                    if (!Metamodel.isDefaultConstructor(constructorModel)) {
                        // constructor name parameter
                        implicitParameterCount++;
                    }
                    int params = meth.getParameterTypes().length - implicitParameterCount;
                    if(params != parameters.size()){
                        defaultedMethods[params - firstDefaulted] = meth;
                        continue;
                    }
                }

                // FIXME: deal with private stuff?
                if(found != null){
                    throw Metamodel.newModelError("More than one constructor method found for: "+javaClass+", 1st: "+found+", 2nd: "+meth);
                }
                found = meth;
            }
        }
        if(found != null){
            boolean variadic = MethodHandleUtil.isVariadicMethodOrConstructor(found);
            ctor = reflectionToMethodHandle(found, javaClass, instance, producedType, parameterProducedTypes, variadic, false);
            if(defaultedMethods != null && !variadic){
                // this won't find the last one, but it's method
                int i=0;
                for(;i<defaultedMethods.length-1;i++){
                    if(defaultedMethods[i] == null)
                        throw Metamodel.newModelError("Missing defaulted constructor for "+ declaration.getName()
                                +" with "+(i+firstDefaulted)+" parameters in "+javaClass);
                    dispatch[i] = reflectionToMethodHandle(defaultedMethods[i], javaClass, instance, producedType, parameterProducedTypes, variadic, false);
                }
                dispatch[i] = ctor;
            }else if(variadic){
                // variadic methods don't have defaulted parameters, but we will simulate one because our calling convention is that
                // we treat variadic methods as if the last parameter is optional
                // firstDefaulted and dispatch already set up because getFirstDefaultedParameter treats java variadics like ceylon variadics
                dispatch[0] = reflectionToMethodHandle(found, javaClass, instance, producedType, parameterProducedTypes, variadic, true);
                dispatch[1] = ctor;
            }
        }
    }

    private MethodHandle reflectionToMethodHandle(Object found, java.lang.Class<?> javaClass, Object instance, 
                                                  com.redhat.ceylon.model.typechecker.model.Type producedType,
                                                  List<com.redhat.ceylon.model.typechecker.model.Type> parameterProducedTypes,
                                                  boolean variadic, boolean bindVariadicParameterToEmptyArray) {
        MethodHandle constructor = null;
        java.lang.Class<?>[] parameterTypes;
        java.lang.Class<?> returnType;
        boolean isJavaArray = MethodHandleUtil.isJavaArray(javaClass);
        boolean isStatic = Modifier.isStatic(javaClass.getModifiers());
        try {
            if(found instanceof java.lang.reflect.Constructor){
                ((java.lang.reflect.Constructor<?>) found).setAccessible(true);
                constructor = MethodHandles.lookup().unreflectConstructor((java.lang.reflect.Constructor<?>)found);
                parameterTypes = ((java.lang.reflect.Constructor<?>)found).getParameterTypes();
                returnType = javaClass;
            }else{
                ((Method)found).setAccessible(true);
                constructor = MethodHandles.lookup().unreflect((Method) found);
                parameterTypes = ((java.lang.reflect.Method)found).getParameterTypes();
                returnType = ((java.lang.reflect.Method)found).getReturnType();
            }
        } catch (IllegalAccessException e) {
            throw Metamodel.newModelError("Problem getting a MH for constructor for: "+javaClass, e);
        }
        boolean isJavaMember = found instanceof java.lang.reflect.Constructor && instance != null && !isStatic;

        // box the return type, which is only necessary for default parameter methods, and not constructors
        constructor = MethodHandleUtil.boxReturnValue(constructor, returnType, producedType);

        // we need to cast to Object because this is what comes out when calling it in $call
        
        // if it's a java member we will be using the member constructor which has an extra synthetic parameter so we can't bind it
        // until we have casted it first
        if(isJavaMember)
            constructor = constructor.asType(MethodType.methodType(Object.class, parameterTypes));
        // now bind it to the object
        if(instance != null
                && (isJavaArray || !isStatic))
            constructor = constructor.bindTo(instance);
        // if it was not a java member we have no extra synthetic instance parameter and we need to get rid of it before casting
        if(!isJavaMember)
            constructor = constructor.asType(MethodType.methodType(Object.class, parameterTypes));
        
        int typeParametersCount = Util.toInt(this.container.getDeclaration().getTypeParameterDeclarations().getSize());
        int skipParameters = 0;
        if(isJavaMember)
            skipParameters++; // skip the first parameter for boxing
        // insert any required type descriptors
        if(typeParametersCount != 0 && MethodHandleUtil.isReifiedTypeSupported(found, isJavaMember)){
            List<com.redhat.ceylon.model.typechecker.model.Type> typeArguments = producedType.getQualifyingType().getTypeArgumentList();
            constructor = MethodHandleUtil.insertReifiedTypeArguments(constructor, 0, typeArguments);
            skipParameters += typeParametersCount;
        }
        // insert any required constructor name parameter
        if(!Metamodel.isDefaultConstructor(declaration.constructor)) {
            constructor = MethodHandleUtil.insertConstructorNameArgument(constructor, 0, declaration.constructor);
            skipParameters++;
        }
        // now convert all arguments (we may need to unbox)
        constructor = MethodHandleUtil.unboxArguments(constructor, skipParameters, 0, parameterTypes,
                                                      parameterProducedTypes, variadic, bindVariadicParameterToEmptyArray);
        
        return constructor;
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type>")
    @Override
    public Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes() {
        return this.parameterTypes;
    }

    @TypeInfo("ceylon.language.meta.model::Class<Type,ceylon.language::Nothing>")
    @Override
    public Class<Type,? extends Object> getContainer() {
        return container;
    }

    @Override
    public ConstructorDeclaration getDeclaration() {
        return declaration;
    }
    

    private void checkConstructor() {
        if(((FreeConstructor)declaration).getContainer().getAbstract())
            throw new InvocationException("Abstract class cannot be instantiated");
    }
    
    @Ignore
    @Override
    public Type $call$() {
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                return (Type)ctor.invokeExact();
            // FIXME: proper checks
            return (Type)dispatch[0].invokeExact();
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }
    
    @Ignore
    @Override
    public Type $call$(Object arg0) {
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                return (Type)ctor.invokeExact(arg0);
            // FIXME: proper checks
            return (Type)dispatch[1-firstDefaulted].invokeExact(arg0);
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @Ignore
    @Override
    public Type $call$(Object arg0, Object arg1) {
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                return (Type)ctor.invokeExact(arg0, arg1);
            // FIXME: proper checks
            return (Type)dispatch[2-firstDefaulted].invokeExact(arg0, arg1);
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @Ignore
    @Override
    public Type $call$(Object arg0, Object arg1, Object arg2) {
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                return (Type)ctor.invokeExact(arg0, arg1, arg2);
            // FIXME: proper checks
            return (Type)dispatch[3-firstDefaulted].invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Ignore
    @Override
    public Type $call$(Object... args) {
        checkInit();
        checkConstructor();
        try {
            if(firstDefaulted == -1)
                // FIXME: this does not do invokeExact and does boxing/widening
                return (Type)ctor.invokeWithArguments(args);
            // FIXME: proper checks
            return (Type)dispatch[args.length-firstDefaulted].invokeWithArguments(args);
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }
    
    @Override
    @Ignore
    public Type $callvariadic$() {
        return $call$();
    }
    
    @Override
    @Ignore
    public Type $callvariadic$(Sequential<?> varargs) {
        return $call$(varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0,
            Sequential<?> varargs) {
        return $call$(arg0, varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0,
            Object arg1, Sequential<?> varargs) {
        return $call$(arg0, arg1, varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0,
            Object arg1, Object arg2, Sequential<?> varargs) {
        return $call$(arg0, arg1, arg2, varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object... argsAndVarargs) {
        return $call$((Object[])argsAndVarargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0) {
        return $call$(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0, Object arg1) {
        return $call$(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2) {
        return $call$(arg0, arg1, arg2, empty_.get_());
    }

    @Ignore
    @Override
    public short $getVariadicParameterIndex$() {
        checkInit();
        return (short)variadicIndex;
    }

    @Ignore
    @Override
    public Type apply(){
        return apply(empty_.get_());
    }

    @TypeInfo(value="Type", erased=true)
    @Override
    public Type apply(@Name("arguments")
        @Sequenced
        @TypeInfo("ceylon.language::Sequential<ceylon.language::Anything>")
        Sequential<?> arguments){
        checkInit();
        checkConstructor();
        return Metamodel.apply(this, arguments, parameterProducedTypes, firstDefaulted, variadicIndex);
    }
    
    @Override
    public Type namedApply(@Name("arguments")
        @TypeInfo("ceylon.language::Iterable<ceylon.language::Entry<ceylon.language::String,ceylon.language::Anything>,ceylon.language::Null>")
        ceylon.language.Iterable<? extends ceylon.language.Entry<? extends ceylon.language.String,? extends java.lang.Object>,? extends java.lang.Object> arguments){
        checkInit();
        checkConstructor();
        
        return Metamodel.namedApply(this, this, 
                (com.redhat.ceylon.model.typechecker.model.Functional)declaration.constructor, 
                arguments, parameterProducedTypes);
    }

    @Override
    public java.lang.String toString() {
        return Metamodel.toTypeString(getContainer()) + "." + getDeclaration().getName();
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof AppliedConstructor) {
            return this.getDeclaration().getName().equals(((AppliedConstructor<?,?>)other).getDeclaration().getName())
                    && this.instance.equals(((AppliedConstructor<?,?>)other).instance);
                    
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        result = 37 * result + (instance == null ? 0 : instance.hashCode());
        result = 37 * result + getDeclaration().hashCode();
        return result;
    }

    @Override
    public Object getDefaultParameterValue(Parameter parameter, Array<Object> values, int collectedValueCount) {
        throw new RuntimeException("not implemented yet");
        /*
        com.redhat.ceylon.compiler.typechecker.model.Class decl = 
                (com.redhat.ceylon.compiler.typechecker.model.Class)declaration.declaration;
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(decl);

        Function found = null;
        java.lang.String name;
        java.lang.Class<?> lookupClass;
        if(!javaClass.isMemberClass()){
            name = "$default$"+parameter.getName();
            lookupClass = javaClass;
        }else{
            name = "$default$" + declaration.getName() + "$" + parameter.getName();
            // FIXME: perhaps store and access the container class literal from an extra param of @Container?
            lookupClass = Metamodel.getJavaClass((Declaration) declaration.declaration.getContainer());
        }
        // iterate to find it, rather than figure out its parameter types
        for(Function m : lookupClass.getDeclaredMethods()){
            if(m.getName().equals(name)){
                found = m;
                break;
            }
        }
        if(found == null)
            throw Metamodel.newModelError("Default argument method for "+parameter.getName()+" not found");
        int parameterCount = found.getParameterTypes().length;
        if(MethodHandleUtil.isReifiedTypeSupported(found, false))
            parameterCount -= found.getTypeParameters().length;
        if(parameterCount != collectedValueCount)
            throw Metamodel.newModelError("Default argument method for "+parameter.getName()+" requires wrong number of parameters: "+parameterCount+" should be "+collectedValueCount);

        // AFAIK default value methods cannot be Java-variadic 
        MethodHandle methodHandle = reflectionToMethodHandle(found, javaClass, instance, producedType, parameterProducedTypes, false, false);
        // sucks that we have to copy the array, but that's the MH API
        java.lang.Object[] arguments = new java.lang.Object[collectedValueCount];
        System.arraycopy(values.toArray(), 0, arguments, 0, collectedValueCount);
        try {
            return methodHandle.invokeWithArguments(arguments);
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }*/
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AppliedConstructor.class, reified$Type, reified$Arguments);
    }
}
