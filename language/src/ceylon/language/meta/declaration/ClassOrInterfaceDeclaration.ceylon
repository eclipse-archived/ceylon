/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language { AnnotationType = Annotation }
import ceylon.language.meta.model {
    ClassOrInterface,
    Member,
    AppliedType = Type,
    IncompatibleTypeException,
    TypeApplicationException
}

"""A class or interface declaration.
   
   <a name="toplevel-sample"></a>
   ### Usage sample for toplevel classes
   
   Because some classes have type parameters, getting a model requires applying type arguments to the
   class declaration with [[apply]] in order to be able to instantiate that class. For example, here is how you would
   obtain a class or interface model that you can instantiate from a toplevel class declaration:
   
       class Foo<T>() {
           string => "Hello, our T is: ``typeLiteral<T>()``";
       }
       
       void test(){
           // We need to apply the Integer closed type to the Foo declaration in order to get the Foo<Integer> closed type
           ClassOrInterface<Foo<Integer>> classOrInterfaceModel = `class Foo`.apply<Foo<Integer>>(`Integer`);
           assert(is Class<Foo<Integer>,[]> classOrInterfaceModel);
           // This will print: Hello, our T is: ceylon.language::Integer
           print(classOrInterfaceModel());
       }
   
   Note that there are more specialised versions of [[apply]] in [[ClassDeclaration.classApply]] and 
   [[InterfaceDeclaration.interfaceApply]].

   <a name="member-sample"></a>
   ### Usage sample for member classes
    
   For member classes or interfaces it is a bit longer, because member types need to be applied not only their type arguments but also
   the containing type, so you should use [[memberApply]] and start by giving the containing closed type:
   
       class Outer(){
           shared class Inner(){
               string => "Hello";
           }
       }
    
       void test(){
           // apply the containing closed type `Outer` to the member class declaration `Outer.Inner`
           value memberClassModel = `class Outer.Inner`.memberApply<Outer,Outer.Inner>(`Outer`);
           assert(is MemberClass<Outer,Outer.Inner,[]> memberClassModel);
           // We now have a MemberClass, which needs to be applied to a containing instance in order to become an
           // invokable class model:
           Class<Outer.Inner,[]> boundMemberClassModel = memberClassModel(Outer());
           // This will print: Hello
           print(boundMemberClassModel());
       }
   
   Note that there are more specialised versions of [[memberApply]] in [[ClassDeclaration.memberClassApply]] and 
   [[InterfaceDeclaration.memberInterfaceApply]].
   """
shared sealed interface ClassOrInterfaceDeclaration 
        of ClassDeclaration | InterfaceDeclaration 
        satisfies NestableDeclaration & GenericDeclaration {
    
    "This type's extended type, unless this is the class for 
     [[Anything|ceylon.language::Anything]], whichis the root 
     of the type hierarchy and thus does not have any extended type."
    shared formal OpenClassType? extendedType;
    
    "The list of types satisfied by this type."
    shared formal OpenInterfaceType[] satisfiedTypes;
    
    "If this type has an `of` clause, this is the list of case types for the current type."
    shared formal OpenType[] caseTypes;
    
    "True if this type is an alias type, in which case the [[extendedType]] will 
     contain the substituted type."
    shared formal Boolean isAlias;
    
    // FIXME: should Kind default to NestableDeclaration?
    "Returns the list of shared member declarations that satisfy the 
     given `Kind` type argument. 
     This includes inherited declarations but not unshared declarations."
    shared formal Kind[] memberDeclarations<Kind>() 
        given Kind satisfies NestableDeclaration;

    "Returns the list of member declarations directly declared on this class or interface, 
     which satisfy the given `Kind` type argument. 
     This includes unshared declarations but not inherited declarations."
    shared formal Kind[] declaredMemberDeclarations<Kind>() 
        given Kind satisfies NestableDeclaration;

    "Returns the list of shared member declarations that satisfy the given `Kind` type argument and
     that are annotated with the given `Annotation` type argument. This includes inherited
     declarations but not unshared declarations."
    shared formal Kind[] annotatedMemberDeclarations<Kind, Annotation>() 
        given Kind satisfies NestableDeclaration
        given Annotation satisfies AnnotationType;
    
    "Returns the list of member declarations directly declared on this class or interface, which satisfy the given 
     `Kind` type argument and that are annotated with the given `Annotation` type argument.
     This includes unshared declarations but not inherited declarations."
    shared formal Kind[] annotatedDeclaredMemberDeclarations<Kind, Annotation>() 
        given Kind satisfies NestableDeclaration
        given Annotation satisfies AnnotationType;
    
    "Looks up a shared member declaration by name, 
     provided it satisfies the given `Kind` type argument. 
     Returns `null` if no such member matches. 
     This includes inherited declarations but not unshared declarations"
    shared formal Kind? getMemberDeclaration<Kind>(String name) 
        given Kind satisfies NestableDeclaration;

    "Looks up a member declaration directly declared on this class or interface, by name, 
     provided it satisfies the given `Kind` type argument. 
     Returns `null` if no such member matches. 
     This includes unshared declarations but not inherited declarations."
    shared formal Kind? getDeclaredMemberDeclaration<Kind>(String name) 
        given Kind satisfies NestableDeclaration;
    
    "Applies the given closed type arguments to this toplevel class or interface declaration in order to obtain a class or interface model. 
     See [this code sample](#toplevel-sample) for an example on how to use this."
    throws(class IncompatibleTypeException, 
        "If the specified `Type` type argument is not compatible with the actual result.")
    throws(class TypeApplicationException, 
            "If the specified closed type argument values are not compatible 
             with the actual result's type parameters.")
    shared formal ClassOrInterface<Type> apply<Type=Anything>(AppliedType<>* typeArguments);
    
    "Applies the given closed container type and type arguments to this member class or interface declaration in order to obtain a 
     member class or interface model. See [this code sample](#member-sample) for an example on how to use this."
    throws(class IncompatibleTypeException, 
        "If the specified `Container` or `Type` type arguments are not 
         compatible with the actual result.")
    throws(class TypeApplicationException, 
            "If the specified closed container type or type argument values 
             are not compatible with the actual result's container type or 
             type parameters.")
    shared formal Member<Container, ClassOrInterface<Type>> & ClassOrInterface<Type> memberApply
            <Container=Nothing, Type=Anything>
            (AppliedType<Object> containerType, AppliedType<>* typeArguments);

}