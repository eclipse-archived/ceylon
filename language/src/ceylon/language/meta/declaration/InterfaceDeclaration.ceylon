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
    Interface,
    MemberInterface,
    AppliedType = Type,
    IncompatibleTypeException,
    TypeApplicationException
}

"""An interface declaration.
   
   <a name="toplevel-sample"></a>
   ### Usage sample for toplevel interfaces
   
   Because some interfaces have type parameters, getting a model requires applying type arguments to the
   interface declaration with [[interfaceApply]] in order to be able to get a closed type. For example, here is how you would
   obtain an interface model from a toplevel interface declaration:
   
       interface Foo<T> satisfies List<T> {
       }
       
       void test(){
           // We need to apply the Integer closed type to the Foo declaration in order to get the Foo<Integer> closed type
           Interface<Foo<Integer>> interfaceModel = `interface Foo`.interfaceApply<Foo<Integer>>(`Integer`);
           // This will print: ceylon.language::List<ceylon.language::Integer>
           for(satisfiedType in interfaceModel.satisfiedTypes){
               print(satisfiedType);
           }
       }
   
   <a name="member-sample"></a>
   ### Usage sample for member interfaces
    
   For member interfaces it is a bit longer, because member interfaces need to be applied not only their type arguments but also
   the containing type, so you should use [[memberInterfaceApply]] and start by giving the containing closed type:
   
       class Outer(){
           shared interface Inner<T> satisfies List<T> {
           }
       }
    
       void test(){
           // apply the containing closed type `Outer` to the member class declaration `Outer.Inner`
           MemberInterface<Outer,Outer.Inner<Integer>> memberInterfaceModel = `interface Outer.Inner`.memberInterfaceApply<Outer,Outer.Inner<Integer>>(`Outer`, `Integer`);
           // This will print: ceylon.language::List<ceylon.language::Integer>
           for(satisfiedType in memberInterfaceModel.satisfiedTypes){
               print(satisfiedType);
           }
       }
   """

shared sealed interface InterfaceDeclaration
        satisfies ClassOrInterfaceDeclaration {
    
    "Applies the given closed type arguments to this toplevel interface 
     declaration in order to obtain an interface model. 
     See [this code sample](#toplevel-sample) for an example on how to use this."
    throws(class IncompatibleTypeException, 
        "If the specified `Type` type argument is not compatible with the 
         actual result.")
    throws(class TypeApplicationException, 
            "If the specified closed type argument values are not compatible 
             with the actual result's type parameters.")
    shared formal Interface<Type> interfaceApply<Type=Anything>(AppliedType<>* typeArguments);

    "Applies the given closed container type and type arguments to this member 
     interface declaration in order to obtain a member interface model. 
     See [this code sample](#member-sample) for an example on how to use this."
    throws(class IncompatibleTypeException, 
        "If the specified `Container` or `Type` type arguments are not 
         compatible with the actual result.")
    throws(class TypeApplicationException, 
            "If the specified closed container type or type argument values 
             are not compatible with the actual result's container type or 
             type parameters.")
    shared formal MemberInterface<Container, Type> memberInterfaceApply<Container=Nothing, Type=Anything>(AppliedType<Object> containerType, AppliedType<>* typeArguments);
}
