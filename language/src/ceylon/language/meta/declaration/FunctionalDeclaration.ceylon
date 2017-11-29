/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model { 
    Applicable,
    FunctionModel,
    Type,
    Qualified,
    IncompatibleTypeException,
    TypeApplicationException
}

"""A function declaration.
   
   <a name="toplevel-sample"></a>
   ### Usage sample for toplevel function
   
   Because some functions have type parameters, getting a model requires applying type arguments to the
   function declaration with [[apply]] in order to be able to invoke that function. For example, here is how you would
   obtain a function model that you can invoke from a toplevel function declaration:
   
       String foo<T>(){
           return "Hello, our T is: ``typeLiteral<T>()``";
       }
       
       void test(){
           // We need to apply the Integer closed type to the foo declaration in order to get the foo<Integer> function model
           Function<String,[]> functionModel = `function foo`.apply<String,[]>(`Integer`);
           // This will print: Hello, our T is: ceylon.language::Integer
           print(functionModel());
       }
   
   <a name="member-sample"></a>
   ### Usage sample for methods
   
   For methods it is a bit longer, because methods need to be applied not only their type arguments but also
   the containing type, so you should use [[memberApply]] and start by giving the containing closed type:
   
       class Outer(){
           shared String hello() => "Hello";
       }
   
       void test(){
           // apply the containing closed type `Outer` to the method declaration `Outer.hello`
           Method<Outer,String,[]> methodModel = `function Outer.hello`.memberApply<Outer,String,[]>(`Outer`);
           // We now have a Method, which needs to be applied to a containing instance in order to become an
           // invokable function:
           Function<String,[]> boundMethodModel = methodModel(Outer());
           // This will print: Hello
           print(boundMethodModel());
       }
   """
shared sealed interface FunctionalDeclaration
        satisfies GenericDeclaration {
    
    "True if the current declaration is an annotation class or function."
    shared formal Boolean annotation;
    
    "The list of parameter declarations for this functional declaration."
    shared formal FunctionOrValueDeclaration[] parameterDeclarations;
    
    "Gets a parameter declaration by name. Returns `null` if no such parameter exists."
    shared formal FunctionOrValueDeclaration? getParameterDeclaration(String name);
    
    
    "Applies the given closed type arguments to this function declaration in 
     order to obtain a function model. 
     See [this code sample](#toplevel-sample) for an example on how to use this."
    throws(class IncompatibleTypeException, 
        "If the specified `Return` or `Arguments` type arguments are 
         not compatible with the actual result.")
    throws(class TypeApplicationException, 
            "If the specified closed type argument values are not compatible 
             with the actual result's type parameters.")
    since("1.2.0")
    shared formal FunctionModel<Return, Arguments>& Applicable<Return, Arguments> 
            apply<Return=Anything, Arguments=Nothing>(Type<>* typeArguments)
            given Arguments satisfies Anything[];
    
    "Applies the given closed container type and type arguments to this 
     method declaration in order to obtain a method model. 
     See [this code sample](#member-sample) for an example on how to use this."
    throws(class IncompatibleTypeException, 
        "If the specified `Container`, `Return` or `Arguments` type arguments 
         are not compatible with the actual result.")
    throws(class TypeApplicationException, 
            "If the specified closed container type or type argument values 
             are not compatible with the actual result's container type or 
             type parameters.")
    since("1.2.0")
    shared formal FunctionModel<Return, Arguments>&Qualified<FunctionModel<Return, Arguments>, Container> 
                memberApply<Container=Nothing, Return=Anything, Arguments=Nothing>(
                    Type<Object> containerType, Type<>* typeArguments)
            given Arguments satisfies Anything[];
    
    "Applies the given closed container type and type arguments to this 
     `static` method declaration in order to obtain a method model."
    since("1.3.1")
    shared formal FunctionModel<Return, Arguments>& Applicable<Return, Arguments> 
            staticApply<Return=Anything, Arguments=Nothing>(
        Type<Object> containerType, Type<>* typeArguments)
            given Arguments satisfies Anything[];
    
    "Invokes the underlying toplevel function, by applying the specified type arguments and value arguments."
    throws(class IncompatibleTypeException, "If the specified type or value arguments are not compatible with this toplevel function.")
    since("1.2.0")
    shared default Anything invoke(Type<>[] typeArguments = [], Anything* arguments)
            => apply<Anything, Nothing>(*typeArguments).apply(*arguments);
    
    "Invokes the underlying method, by applying the specified type arguments and value arguments."
    throws(class IncompatibleTypeException, "If the specified container, type or value arguments are not compatible with this method.")
    since("1.2.0")
    shared formal Anything memberInvoke(Object container, Type<>[] typeArguments = [], Anything* arguments)/*
            => memberApply<Nothing, Anything, Nothing>(`Nothing`, *typeArguments).bind(container).apply(*arguments)*/;
    
    "Invokes the `static` method, by applying the specified container type, 
     type arguments and value arguments."
    since("1.3.1")
    shared default Anything staticInvoke(Type<Object> containerType, Type<>[] typeArguments = [], Anything* arguments) 
            => staticApply<Anything>(containerType, *typeArguments).apply(*arguments);
}
