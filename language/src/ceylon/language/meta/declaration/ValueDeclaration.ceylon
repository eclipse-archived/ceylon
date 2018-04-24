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
    Value, 
    Attribute, 
    AppliedType = Type, 
    IncompatibleTypeException, 
    StorageException 
}

"""A value declaration.
   
   <a name="toplevel-sample"></a>
   ### Usage sample for toplevel value
   
   Getting a model requires applying type arguments to the
   value declaration with [[apply]] in order to be able to read that value. For example, here is how you would
   obtain a value model that you can read from a toplevel attribute declaration:
   
       String foo = "Hello";
       
       void test(){
           // We need to apply the the foo declaration in order to get the foo value model
           Value<String> valueModel = `value foo`.apply<String>();
           // This will print: Hello
           print(valueModel.get());
       }
   
   <a name="member-sample"></a>
   ### Usage sample for attributes
    
   For attributes it is a bit longer, because attributes need to be applied the containing type, so you should 
   use [[memberApply]] and start by giving the containing closed type:
   
       class Outer(){
           shared String foo => "Hello";
       }
    
       void test(){
           // Apply the containing closed type `Outer` to the attribute declaration `Outer.foo`
           Attribute<Outer,String> valueModel = `value Outer.foo`.memberApply<Outer,String>(`Outer`);
           // We now have an Attribute, which needs to be applied to a containing instance in order to become a
           // readable value:
           Value<String> boundValueModel = valueModel(Outer());
           // This will print: Hello
           print(boundValueModel.get());
       }
   """
shared sealed interface ValueDeclaration
        satisfies FunctionOrValueDeclaration & GettableDeclaration {
    
    "True if this declaration is annotated with [[late|ceylon.language::late]]."
    since("1.2.0")
    shared formal Boolean late;
    
    "True if this declaration is annotated with [[variable|ceylon.language::variable]]."
    shared formal Boolean variable;

    "True if this declaration is an `object` declaration, whose type is an anonymous class."
    since("1.1.0")
    shared formal Boolean objectValue;
    
    "This value's anonymous class declaration if this value is an object declaration. `null` otherwise."
    since("1.1.0")
    shared formal ClassDeclaration? objectClass;

    "Applies this value declaration in order to obtain a value model. 
     See [this code sample](#toplevel-sample) for an example on how to use this."
    throws(class IncompatibleTypeException, "If the specified `Get` or `Set` type arguments are not compatible with the actual result.")
    shared formal Value<Get, Set> apply<Get=Anything, Set=Nothing>();
    
    "Applies this `static` value declaration in order to obtain a value model."
    throws(class IncompatibleTypeException, "If the specified `Get` or `Set` type arguments are not compatible with the actual result.")
    shared formal Value<Get, Set> staticApply<Get=Anything, Set=Nothing>(AppliedType<Object> containerType);

    "Applies the given closed container type to this attribute declaration in order to obtain an attribute model. 
     See [this code sample](#member-sample) for an example on how to use this."
    throws(class IncompatibleTypeException, "If the specified `Container`, `Get` or `Set` type arguments are not compatible with the actual result.")
    shared formal Attribute<Container, Get, Set> memberApply<Container=Nothing, Get=Anything, Set=Nothing>(AppliedType<Object> containerType);
    
    "Reads the current value of this toplevel value."
    shared actual default Anything get()
            => apply<Anything, Nothing>().get();
    
    "Reads the current value of this attribute on the given container instance."
    throws(class IncompatibleTypeException, "If the specified container is not compatible with this attribute.")
    throws(class StorageException,
           "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared actual default Anything memberGet(Object container)
            => memberApply<Nothing, Anything, Nothing>(`Nothing`).bind(container).get();

    "Reads the current value of this `static` attribute."
    throws(class IncompatibleTypeException, "If the specified `Get` or `Set` type arguments are not compatible with the actual result.")
    shared actual default Anything staticGet(AppliedType<Object> containerType)
            => staticApply<Anything, Nothing>(containerType).get();

    "Sets the current value of this toplevel value."
    shared default void set(Anything newValue)
        => apply<Anything,Nothing>().setIfAssignable(newValue);

    "Sets the current value of this attribute on the given container instance."
    throws(class IncompatibleTypeException, "If the specified container or new value type is not compatible with this attribute.")
    throws(class StorageException,
           "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared formal void memberSet(Object container, Anything newValue);
    //=> memberApply<Nothing, Anything, Nothing>(`Nothing`).bind(container).setIfAssignable(newValue);

    "Returns the setter declaration for this variable if there is one, `null` otherwise."
    shared formal SetterDeclaration? setter;

}