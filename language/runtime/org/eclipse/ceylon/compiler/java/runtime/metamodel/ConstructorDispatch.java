/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.ConstructorName;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Jpa;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.CallableConstructorDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ClassDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.ClassImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.MemberClassImpl;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.Reference;

import ceylon.language.Array;
import ceylon.language.Callable;
import ceylon.language.Entry;
import ceylon.language.Iterable;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.model.Applicable;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.meta.model.InvocationException;

/**
 * Dispatches to constructors
 * 
 * @author tom
 * @param <Type>
 * @param <Arguments>
 */
public class ConstructorDispatch<Type, Arguments extends Sequential<? extends Object>> 
    implements Callable<Type>, DefaultValueProvider, Applicable<Type, Arguments>  {

    private final ClassDeclarationImpl freeClass;
    private final CallableConstructorDeclarationImpl freeConstructor;
    private MethodHandle constructor;
    public final int firstDefaulted;
    public final int variadicIndex;
    private MethodHandle[] dispatch;
    public final List<org.eclipse.ceylon.model.typechecker.model.Type> parameterProducedTypes;
    final Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> parameterTypes;
    final Object instance;
    private Reference constructorReference;// TODO get rid of this
    
    public ConstructorDispatch(
            Reference constructorReference,
            ClassModel<Type, ?> appliedClass,
            CallableConstructorDeclarationImpl freeConstructor,
            List<Parameter> parameters, Object instance) {
        this.constructorReference = constructorReference;
        freeClass = (ClassDeclarationImpl)
                (appliedClass instanceof ClassImpl ?
                        ((ClassImpl)appliedClass).declaration : 
                        ((MemberClassImpl)appliedClass).declaration);
        this.freeConstructor = freeConstructor;
        org.eclipse.ceylon.model.typechecker.model.Class classDecl = (org.eclipse.ceylon.model.typechecker.model.Class)freeClass.declaration;
        //org.eclipse.ceylon.model.typechecker.model.Constructor decl = freeConstructor.constructor;
        //List<Parameter> parameters = decl.getFirstParameterList().getParameters();
        this.firstDefaulted = Metamodel.getFirstDefaultedParameter(parameters);
        this.variadicIndex = Metamodel.getVariadicParameter(parameters);

        boolean invokeOnCompanionInstance = instance != null 
                && classDecl.getContainer() instanceof org.eclipse.ceylon.model.typechecker.model.Interface
                && (freeConstructor != null || !classDecl.isShared());
        if (invokeOnCompanionInstance) {
            this.instance = Metamodel.getCompanionInstance(instance, (org.eclipse.ceylon.model.typechecker.model.Interface)freeClass.declaration.getContainer());
        } else {
            this.instance = instance;
        }
        
        if(firstDefaulted != -1){
            // if we have 2 params and first is defaulted we need 2 + 1 - 0 = 3 methods:
            // f(), f(a) and f(a, b)
            this.dispatch = new MethodHandle[parameters.size() + 1 - firstDefaulted];
        } else {
            this.dispatch = new MethodHandle[1];
        }

        // get a list of produced parameter types
        this.parameterProducedTypes = Metamodel.getParameterProducedTypes(parameters, constructorReference);
        this.parameterTypes = Metamodel.getAppliedMetamodelSequential(this.parameterProducedTypes);

        // FIXME: delay constructor setup for when we actually use it?
        // FIXME: finding the right MethodHandle for the constructor could actually be done in the Class declaration
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(freeClass.declaration);
        // FIXME: faster lookup with types? but then we have to deal with erasure and stuff
        Member found = null;
        Member[] defaultedMethods;
        if (freeConstructor != null && ModelUtil.isConstructor(freeConstructor.declaration)) {
            if(MethodHandleUtil.isJavaArray(javaClass)){
                // *Array.with constructor
                found =  MethodHandleUtil.setupArrayWithConstructor(javaClass);
            } else {
                namedConstructorDispatch(constructorReference, freeConstructor,
                        javaClass); 
            }
            defaultedMethods = null;
        } else {
            defaultedMethods = firstDefaulted != -1 ? new Member[dispatch.length] : null;
            if(MethodHandleUtil.isJavaArray(javaClass)){
                found = MethodHandleUtil.setupArrayConstructor(javaClass, defaultedMethods);
            }else if(!(javaClass.isMemberClass() && !Modifier.isStatic(javaClass.getModifiers())) 
                    || !Metamodel.isCeylon((org.eclipse.ceylon.model.typechecker.model.Class)freeClass.declaration)
                    // private ceylon member classes don't have any outer constructor method so treat them like java members
                    || !classDecl.isShared()){
                    // find the MH for the primary constructor for a 
                    // a Celon class which maps to a java constructor
                    // or a Java class
                defaultedMethods = findConstructors(freeConstructor, javaClass);
                int i=0;
                for(;i<defaultedMethods.length;i++){
                    if(defaultedMethods[i] == null)
                        throw Metamodel.newModelError("Missing defaulted constructor for "+ freeClass.getName()
                                +" with "+(i+firstDefaulted)+" parameters in "+javaClass);
                    dispatch[i] = reflectionToMethodHandle(constructorReference, 
                            defaultedMethods[i], 
                            javaClass, 
                            this.instance, 
                            parameterProducedTypes, 
                            ((Constructor)defaultedMethods[i]).isVarArgs(), 
                            false);
                }
                constructor = dispatch[defaultedMethods.length-1];
                if (constructor == null) {
                    throw new NullPointerException();
                }
                
            }else{
                // find the MH for the primary instantiatorfor a 
                // A ceylon member class
                // FIXME: this probably doesn't work for local classes
                // FIXME: perhaps store and access the container class literal from an extra param of @Container?
                java.lang.Class<?> outerJavaClass = Metamodel.getJavaClass((Declaration) freeClass.declaration.getContainer());
                defaultedMethods = findInstantiators(freeConstructor, outerJavaClass);
                int i=0;
                for(;i<defaultedMethods.length;i++){
                    if(defaultedMethods[i] == null)
                        throw Metamodel.newModelError("Missing defaulted constructor for "+ freeClass.getName()
                                +" with "+(i+firstDefaulted)+" parameters in "+javaClass);
                    dispatch[i] = reflectionToMethodHandle(constructorReference, 
                            defaultedMethods[i], 
                            javaClass, 
                            this.instance, 
                            parameterProducedTypes, 
                            false, 
                            false);
                }
                constructor = dispatch[defaultedMethods.length-1];
                if (constructor == null) {
                    throw new NullPointerException();
                }
            }
        }
        
        if(found != null){
            // now find the overloads
            boolean jvmVarargs = MethodHandleUtil.isJvmVarargsMethodOrConstructor(found);
            constructor = reflectionToMethodHandle(constructorReference, 
                    found, 
                    javaClass, this.instance,
                    parameterProducedTypes, 
                    jvmVarargs, 
                    false);
            if(defaultedMethods != null && !jvmVarargs){
                // this won't find the last one, but it's method
                int i=0;
                for(;i<defaultedMethods.length-1;i++){
                    if(defaultedMethods[i] == null)
                        throw Metamodel.newModelError("Missing defaulted constructor for "+ freeClass.getName()
                                +" with "+(i+firstDefaulted)+" parameters in "+javaClass);
                    dispatch[i] = reflectionToMethodHandle(constructorReference, 
                            defaultedMethods[i], 
                            javaClass, 
                            this.instance, 
                            parameterProducedTypes, 
                            jvmVarargs, 
                            false);
                }
                dispatch[i] = constructor;
            }else if(jvmVarargs){
                // variadic methods don't have defaulted parameters, but we will simulate one because our calling convention is that
                // we treat variadic methods as if the last parameter is optional
                // firstDefaulted and dispatch already set up because getFirstDefaultedParameter treats java variadics like ceylon variadics
                dispatch[0] = reflectionToMethodHandle(constructorReference, 
                        found, 
                        javaClass, 
                        this.instance,
                        parameterProducedTypes, 
                        jvmVarargs, 
                        true);
                dispatch[1] = constructor;
            }
        }
    }
    /*
     * This one came from AppliedClass
    private MethodHandle reflectionToMethodHandle(Member found, Class<?> javaClass,  
                                                  org.eclipse.ceylon.model.typechecker.model.Type producedType,
                                                  List<org.eclipse.ceylon.model.typechecker.model.Type> parameterProducedTypes,
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
        
        int typeParametersCount = Util.toInt(this.freeClass.getTypeParameterDeclarations().getSize());
        int skipParameters = 0;
        if(isJavaMember)
            skipParameters++; // skip the first parameter for boxing
        // insert any required type descriptors
        if(typeParametersCount != 0 && MethodHandleUtil.isReifiedTypeSupported(found, isJavaMember)){
            List<org.eclipse.ceylon.model.typechecker.model.Type> typeArguments = producedType.getTypeArgumentList();
            constructor = MethodHandleUtil.insertReifiedTypeArguments(constructor, 0, typeArguments);
            skipParameters += typeParametersCount;
        }
        // now convert all arguments (we may need to unbox)
        constructor = MethodHandleUtil.unboxArguments(constructor, skipParameters, 0, parameterTypes,
                                                      parameterProducedTypes, variadic, bindVariadicParameterToEmptyArray);
        
        return constructor;
    }*/

    protected void namedConstructorDispatch(Reference constructorReference,
            CallableConstructorDeclarationImpl freeConstructor,
            java.lang.Class<?> javaClass) {
        // find the MH for the primary constructor for a Ceylon constructor
        if (firstDefaulted != -1) {
            org.eclipse.ceylon.model.typechecker.model.Class classDecl = (org.eclipse.ceylon.model.typechecker.model.Class)freeClass.declaration;
            final Member[] defaultedMethods;
            if (!javaClass.isMemberClass() 
                    || !Metamodel.isCeylon((org.eclipse.ceylon.model.typechecker.model.Class)freeClass.declaration)
                    // private ceylon member classes don't have any outer constructor method so treat them like java members
                    || !(classDecl.isShared() && freeConstructor.getShared())) {
                defaultedMethods = findConstructors(freeConstructor, javaClass);
            } else {
                defaultedMethods = findInstantiators(freeConstructor, javaClass.getEnclosingClass());
            }
            int i=0;
            for(;i<defaultedMethods.length;i++){
                if(defaultedMethods[i] == null)
                    throw Metamodel.newModelError("Missing defaulted constructor for "+ freeClass.getName()
                            +" with "+(i+firstDefaulted)+" parameters in "+javaClass);
                dispatch[i] = reflectionToMethodHandle(constructorReference, 
                        defaultedMethods[i], 
                        javaClass, 
                        this.instance, 
                        parameterProducedTypes, 
                        false, 
                        false);
            }
            constructor = dispatch[defaultedMethods.length-1];
        } else {
            // TODO this doesn't really need to be a separate branch from the above.
            java.lang.reflect.Constructor<?> ctor = Metamodel.getJavaConstructor(ModelUtil.getConstructor(freeConstructor.declaration));
            constructor = reflectionToMethodHandle(constructorReference, 
                    ctor, 
                    javaClass, 
                    this.instance, 
                    parameterProducedTypes, 
                    false,// a Ceylon constructor is never jvm varargs (...) 
                    false);
        }
    }

    protected Constructor<?>[] findConstructors(
            CallableConstructorDeclarationImpl freeConstructor,
            java.lang.Class<?> javaClass) {
        final Constructor<?>[] defaultedMethods = new Constructor[dispatch.length];
        String ctorName = freeConstructor == null ? null : freeConstructor.declaration.getName();
        outer: for(Constructor<?> constr : javaClass.getDeclaredConstructors()) {
            if (constr.isAnnotationPresent(Jpa.class)) {
                continue;
            }
            // only ignore hand-written ignore annotations otherwise we're ignoring defaulted constructors
            // which we don't want to
            Ignore ignoreAnnotation = constr.getAnnotation(Ignore.class);
            if(ignoreAnnotation != null
                    && (ignoreAnnotation.handWritten() 
                            || constr.getParameterTypes().length==1 
                            && constr.getParameterTypes()[0].getName()
                                .endsWith(".$Serialization$")))
                continue;
            int numTypeParameters = 0;
            int ii = 0;
            boolean jvmVarargs = MethodHandleUtil.isJvmVarargsMethodOrConstructor(constr);
            Class<?>[] pts = constr.getParameterTypes();
            for (Class<?> pt : pts) {
                // TODO need to exclude the serialization constructor too!
                // TODO what if we find more constructors than defaulted methods contains?
                // TODO badly need to abstract/refactor this shit, 
                // so there's just one API for figuring out the dispatch array.
                if (ii == 0 && javaClass.isMemberClass() && !Modifier.isStatic(javaClass.getModifiers())) {
                    // ignore the hidden outer instance parameter
                    ii++;
                    continue;
                }
                if (TypeDescriptor.class.isAssignableFrom(pt)) {
                    // ignore the type descriptors
                    numTypeParameters++;
                    ii++;
                    continue;
                }
                if (numTypeParameters != -1) {
                    if (numTypeParameters != ((org.eclipse.ceylon.model.typechecker.model.Class)freeClass.declaration).getTypeParameters().size()) {
                        // type parameter's don't match
                        continue outer;
                    }
                    numTypeParameters = -1;
                }
                ConstructorName annotation = pt.getAnnotation(ConstructorName.class);
                if (ctorName != null) {
                    if (annotation != null
                        && ctorName.equals(annotation.value())
                        && !annotation.delegation()) {
                        ii++;
                        defaultedMethods[index(ii, pts, jvmVarargs)] = constr;
                    }
                } else {
                    // the default constructor
                    if (annotation == null) {
                        defaultedMethods[index(ii, pts, jvmVarargs)] = constr;
                        if (variadicIndex != -1 && jvmVarargs) {
                            defaultedMethods[variadicIndex-1] = constr;
                        }
                    }
                }
                continue outer;
            }
            if (freeConstructor == null || (ctorName == null || ctorName.isEmpty())) {
                defaultedMethods[index(ii, pts, jvmVarargs)] = constr;
            }
        }
        return defaultedMethods;
    }
    
    int index(int ii, Class<?>[] pts, boolean jvmVarargs) {
        if (firstDefaulted ==  -1) {
            return 0;
        } else {
            if (variadicIndex == -1) {
                return pts.length-ii-firstDefaulted;
            } else {
                return jvmVarargs ? 0 : pts.length-ii-firstDefaulted;
            }
        }
        
    }
    
    protected Method[] findInstantiators(
            CallableConstructorDeclarationImpl freeConstructor,
            java.lang.Class<?> javaClass) {
        String builderName = freeClass.getName() + "$new$";
        final Method[] defaultedMethods = new Method[dispatch.length];
        String ctorName = freeConstructor == null ? null : freeConstructor.declaration.getName();
        outer: for(Method constr : javaClass.getDeclaredMethods()) {
            if (constr.isSynthetic()) {
                continue;
            }
            if (!constr.getName().equals(builderName)) {
                continue;
            }
            int ii = 0;
            Class<?>[] pts = constr.getParameterTypes();
            if (pts.length == 0) {
                defaultedMethods[0] = constr;
                continue outer;
            }
            boolean jvmVarargs = MethodHandleUtil.isJvmVarargsMethodOrConstructor(constr);
            for (Class<?> pt : pts) {
                // TODO need to exclude the serialization constructor too!
                // TODO what if we find more constructors than defaulted methods contains?
                // TODO badly need to abstract/refactor this shit, 
                // so there's just one API for figuring out the dispatch array.
                if (TypeDescriptor.class.isAssignableFrom(pt)) {
                    ii++;
                    continue;
                }
                ConstructorName annotation = pt.getAnnotation(ConstructorName.class);
                if (ctorName != null) {
                    if (annotation != null
                        && ctorName.equals(annotation.value())
                        && !annotation.delegation()) {
                        ii++;
                        defaultedMethods[index(ii, pts, jvmVarargs)] = constr;
                    }
                } else {
                    // the default constructor
                    if (annotation == null) {
                        defaultedMethods[index(ii, pts, jvmVarargs)] = constr;
                    }
                }
                continue outer;
            }
        }
        return defaultedMethods;
    }
    
    private static MethodHandle reflectionToMethodHandle(
            Reference constructorReference,
            Member found, 
            java.lang.Class<?> javaClass, 
            Object instance, 
            List<org.eclipse.ceylon.model.typechecker.model.Type> parameterProducedTypes,
            boolean jvmVarargs, 
            boolean bindVariadicParameterToEmptyArray) {
        // save the parameter types before we mess with "found"
        java.lang.Class<?>[] parameterTypes;
        java.lang.Class<?> returnType;
        MethodHandle method = null;
        boolean isJavaArray;
        boolean isStatic;
        int typeParametersCount;
        int skipParameters = 0;
        List<org.eclipse.ceylon.model.typechecker.model.TypeParameter> reifiedTypeParameters;
        org.eclipse.ceylon.model.typechecker.model.Constructor constructorModel;
        if (found instanceof java.lang.reflect.Method) {
            org.eclipse.ceylon.model.typechecker.model.Generic functionModel = (org.eclipse.ceylon.model.typechecker.model.Generic)constructorReference.getDeclaration();
            java.lang.reflect.Method foundMethod = (java.lang.reflect.Method)found;
            parameterTypes = foundMethod.getParameterTypes();
            returnType = foundMethod.getReturnType();
            isStatic = Modifier.isStatic(foundMethod.getModifiers());
            isJavaArray = MethodHandleUtil.isJavaArray(javaClass);
            typeParametersCount = javaClass.getTypeParameters().length;
            try {
                if(isJavaArray){
                    if(foundMethod.getName().equals("get"))
                        method = MethodHandleUtil.getJavaArrayGetterMethodHandle(javaClass);
                    else if(foundMethod.getName().equals("set"))
                        method = MethodHandleUtil.getJavaArraySetterMethodHandle(javaClass);
                    else if(foundMethod.getName().equals("copyTo")){
                        found = MethodHandleUtil.getJavaArrayCopyToMethod(javaClass, foundMethod);
                    }
                }
                if(method == null){
                    foundMethod.setAccessible(true);
                    method = MethodHandles.lookup().unreflect(foundMethod);
                }
            } catch (IllegalAccessException e) {
                throw Metamodel.newModelError("Problem getting a MH for constructor for: "+javaClass, e);
            }
            reifiedTypeParameters = functionModel.getTypeParameters();
            if(isJavaArray && "objectArrayConstructor".equals(foundMethod.getName())){
                reifiedTypeParameters = ((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)((Declaration)functionModel).getContainer()).getTypeParameters();
            }
            constructorModel = null;
        } else if (found instanceof java.lang.reflect.Constructor<?>) {
            org.eclipse.ceylon.model.typechecker.model.Declaration functionOrConstructorModel = constructorReference.getDeclaration();
            java.lang.reflect.Constructor<?> foundMethod = (java.lang.reflect.Constructor<?>)found;
            parameterTypes = foundMethod.getParameterTypes();
            returnType = javaClass;
            isStatic = Modifier.isStatic(foundMethod.getDeclaringClass().getModifiers());
            foundMethod.setAccessible(true);
            try {
                method = MethodHandles.lookup().unreflectConstructor(foundMethod);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isJavaArray = false;
            typeParametersCount = javaClass.getTypeParameters().length;
            if (javaClass.isMemberClass() && Modifier.isStatic(javaClass.getModifiers())) {
                typeParametersCount += javaClass.getEnclosingClass().getTypeParameters().length;
            }
            if (functionOrConstructorModel instanceof org.eclipse.ceylon.model.typechecker.model.Constructor) {
                constructorModel = (org.eclipse.ceylon.model.typechecker.model.Constructor)functionOrConstructorModel;
                reifiedTypeParameters = ModelUtil.getConstructedClass(constructorModel).getTypeParameters();
            } else if (functionOrConstructorModel instanceof Function) {
                constructorModel = (org.eclipse.ceylon.model.typechecker.model.Constructor)((Function)functionOrConstructorModel).getTypeDeclaration();
                reifiedTypeParameters = ModelUtil.getConstructedClass(constructorModel).getTypeParameters();
            } else if (functionOrConstructorModel instanceof org.eclipse.ceylon.model.typechecker.model.Class) {
                constructorModel = null;
                reifiedTypeParameters = ((org.eclipse.ceylon.model.typechecker.model.Class)functionOrConstructorModel).getTypeParameters();
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException();
        }
        boolean isJavaMember = 
                found instanceof java.lang.reflect.Constructor 
                && instance != null 
                && !isStatic;
        // box the return type
        method = MethodHandleUtil.boxReturnValue(method, returnType, constructorReference.getType());
        // we need to cast to Object because this is what comes out when calling it in $call
        
        
        // if it's a java member we will be using the member constructor which has an extra synthetic parameter so we can't bind it
        // until we have casted it first
        if(isJavaMember) {
            method = method.asType(MethodType.methodType(Object.class, parameterTypes));
        }
        if(instance != null 
                && (isJavaArray || !isStatic)) {
            method = method.bindTo(instance);
        }
     // if it was not a java member we have no extra synthetic instance parameter and we need to get rid of it before casting
        if(!isJavaMember) {
            method = method.asType(MethodType.methodType(Object.class, parameterTypes));
        }
        if(isJavaMember) {
            skipParameters++; // skip the first parameter for boxing
        }
        // insert any required type descriptors
        if(typeParametersCount != 0 && MethodHandleUtil.isReifiedTypeSupported(found, isJavaMember)){
            List<org.eclipse.ceylon.model.typechecker.model.Type> typeArguments = new ArrayList<org.eclipse.ceylon.model.typechecker.model.Type>();
            java.util.Map<org.eclipse.ceylon.model.typechecker.model.TypeParameter, org.eclipse.ceylon.model.typechecker.model.Type> typeArgumentMap = constructorReference.collectTypeArguments();
            for (org.eclipse.ceylon.model.typechecker.model.TypeParameter tp : reifiedTypeParameters) {
                typeArguments.add(typeArgumentMap.get(tp));
            }
            method = MethodHandleUtil.insertReifiedTypeArguments(method, 0, typeArguments);
            skipParameters += typeParametersCount;
        }
        // Now, if it's a constructor we need to insert the (null) constructor name argument
        if (constructorModel != null
                && constructorModel.getName() != null
                && !constructorModel.getName().isEmpty()) {
            method = MethodHandleUtil.insertConstructorNameArgument(method, 0, constructorModel);
            skipParameters += 1;
        }
        // now convert all arguments (we may need to unbox)
        method = MethodHandleUtil.unboxArguments(method, skipParameters, 0, parameterTypes,
                parameterProducedTypes, jvmVarargs, bindVariadicParameterToEmptyArray);
        return method;
    }
    
    public void checkConstructor() {
        if(((ClassDeclarationImpl)freeClass).getAbstract())
            throw new InvocationException("Abstract class cannot be instantiated");
        if(((ClassDeclarationImpl)freeClass).getAnonymous())
            throw new InvocationException("Object class cannot be instantiated");
        if(constructor == null)
            throw Metamodel.newModelError("No constructor found for: "+freeClass.getName());
    }
    
    @Override
    public Type $call$() {
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
        try {
            if(firstDefaulted == -1)
                return (Type)constructor.invokeWithArguments(args);
            // FIXME: proper checks
            return (Type)dispatch[args.length-firstDefaulted].invokeWithArguments(args);
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }
    
    @Override
    public Type $callvariadic$(Object... argsAndVarargs) {
        return $call$(argsAndVarargs);
    }
    
    @Override
    public short $getVariadicParameterIndex$() {
        return (short)variadicIndex;
    }
    
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes(){
        return parameterTypes;
    }

    public List<org.eclipse.ceylon.model.typechecker.model.Type> getProducedParameterTypes() {
        return parameterProducedTypes;
    }
    
    
    @Override
    public Object getDefaultParameterValue(Parameter parameter, Array<Object> values, int collectedValueCount) {
        org.eclipse.ceylon.model.typechecker.model.Class decl = 
                (org.eclipse.ceylon.model.typechecker.model.Class)freeClass.declaration;
        java.lang.Class<?> javaClass = Metamodel.getJavaClass(decl);

        Method found = null;
        String name;
        java.lang.Class<?> lookupClass;
        if(!javaClass.isMemberClass()){
            name = "$default$"+parameter.getName();
            lookupClass = javaClass;
        }else{
            name = "$default$" + freeClass.getName() + "$" + parameter.getName();
            // FIXME: perhaps store and access the container class literal from an extra param of @Container?
            lookupClass = Metamodel.getJavaClass((Declaration) freeClass.declaration.getContainer());
        }
        // iterate to find it, rather than figure out its parameter types
        for(Method m : lookupClass.getDeclaredMethods()){
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
        MethodHandle methodHandle = reflectionToMethodHandle(constructorReference, found, 
                javaClass, this.instance, parameterProducedTypes, false, false);
        // sucks that we have to copy the array, but that's the MH API
        java.lang.Object[] arguments = new java.lang.Object[collectedValueCount];
        System.arraycopy(values.toArray(), 0, arguments, 0, collectedValueCount);
        try {
            return methodHandle.invokeWithArguments(arguments);
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @Override
    public Type apply() {
        return apply(empty_.get_());
    }

    @Override
    public Type apply(Sequential<? extends Object> arguments) {
        checkConstructor();
        return Metamodel.apply(this, arguments, parameterProducedTypes, firstDefaulted, variadicIndex);
    }

    @Override
    public Type namedApply(
            Iterable<? extends Entry<? extends ceylon.language.String, ? extends Object>, ? extends Object> arguments) {
        checkConstructor();
        return Metamodel.namedApply(this, this, 
                (org.eclipse.ceylon.model.typechecker.model.Functional)(freeConstructor != null ? freeConstructor.declaration : freeClass.declaration), 
                arguments, parameterProducedTypes);
    }
}
