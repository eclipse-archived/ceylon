/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration {
    ClassOrInterfaceDeclaration
}

import ceylon.language.meta.model {
    ClosedType = Type
}

"Model of a class or interface that you can inspect.
 
 The models of classes and interfaces are also closed types."
shared sealed interface ClassOrInterface<out Type=Anything> 
    of ClassModel<Type, Nothing> | InterfaceModel<Type>
    satisfies Model & Generic & ClosedType<Type> {
    
    "The declaration for this class or interface."
    shared formal actual ClassOrInterfaceDeclaration declaration;

    "The extended closed type for this class or interface. 
     Note that the [[Anything|ceylon.language::Anything]] type
     has no extended type since it is the top of the type hierarchy."
    shared formal ClassModel<>? extendedType;
    
    "The list of closed types that this class or interface satisfies."
    shared formal InterfaceModel<>[] satisfiedTypes;

    "The list of case values for this type. This omits any case type to 
     only contain case values."
    since("1.1.0")
    shared formal Type[] caseValues;

    // FIXME: move all these to Type
    // FIXME: introduce MemberClassOrInterface?
    // if I do that I have to give up the enumerated type of ClassModel | InterfaceModel here, so let's not do that for now,
    // since I don't quite see what we would gain

    "Gets a member class or interface by name. Returns `null` if not found.
     
     The [[Container]] type parameter acts as a selector for the container of the member:
     
     - If it is the current type or a subtype, and the attribute exists in this type, then the attribute
       of the current type is returned. For example, looking up 'unit' in [[Integer]] with [[Integer]] as [[Container]]
       will return [[Integer.unit]].
     - If it is a supertype of this type, and that supertype defines this attribute, then that supertype's
       attribute is returned. For example, looking up 'hash' in [[Integer]] with [[Object]] as [[Container]] will return [[Object.hash]].
     - If it is a type that is disjoint from this type, the attribute will be looked up in the common supertype
       between that type and this type. For example, looking up 'hash' in [[Integer]] with [[String]] as [[Container]] 
       will return [[Object.hash]].
     - If the common supertype between this type and the [[Container]] type has no such member, `null` is returned.
     "
    throws(class IncompatibleTypeException, 
           "If the specified `Kind` type argument is not compatible with the actual result.")
    throws(class TypeApplicationException, 
           "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal Member<Container, Kind>? getClassOrInterface
            <Container=Nothing, Kind=ClassOrInterface<>>
            (String name, ClosedType<Anything>* types)
        given Kind satisfies ClassOrInterface<Anything>;

    "Gets a member class or interface by name. Returns `null` if not found.
     
     The [[Container]] type parameter acts as a selector for the container of the member:
     
     - If it is the current type or a subtype, and the attribute exists in this type, then the attribute
       of the current type is returned. For example, looking up 'unit' in [[Integer]] with [[Integer]] as [[Container]]
       will return [[Integer.unit]].
     - If it is a supertype of this type, or a type that is disjoint from this type, an [[IncompatibleTypeException]] will
       be thrown.
     - If this type has no such member, `null` is returned.
     "
    throws(class IncompatibleTypeException, 
           "If the specified `Container` or `Kind` type arguments are not compatible with the actual result.")
    throws(class TypeApplicationException, 
           "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal Member<Container, Kind>? getDeclaredClassOrInterface
            <Container=Nothing, Kind=ClassOrInterface<>>
            (String name, ClosedType<Anything>* types)
        given Kind satisfies ClassOrInterface<Anything>;

    "Gets a member class by name. Returns `null` if not found.
     
     See [[getClassOrInterface]] for how the [[Container]] type parameter works."
    throws(class IncompatibleTypeException, 
           "If the specified `Type` or `Arguments` type arguments are not compatible with the actual result, 
            or if the corresponding member is not a `MemberClass`.")
    throws(class TypeApplicationException, 
           "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal MemberClass<Container, Type, Arguments>? getClass
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (String name, ClosedType<Anything>* types)
        given Arguments satisfies Anything[];

    "Gets a member class by name. Returns `null` if not found.
     
     See [[getDeclaredClassOrInterface]] for how the [[Container]] type parameter works."
    throws(class IncompatibleTypeException, 
           "If the specified `Container`, `Type` or `Arguments` type arguments are not compatible with the actual result, 
            or if the corresponding member is not a `MemberClass`.")
    throws(class TypeApplicationException, 
           "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal MemberClass<Container, Type, Arguments>? getDeclaredClass
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (String name, ClosedType<Anything>* types)
        given Arguments satisfies Anything[];

    "Gets a member interface by name. Returns `null` if not found.
     
     See [[getClassOrInterface]] for how the [[Container]] type parameter works."
    throws(class IncompatibleTypeException, 
           "If the specified `Type` type argument is not compatible with the actual result, 
            or if the corresponding member is not a `MemberInterface`.")
    throws(class TypeApplicationException, 
           "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal MemberInterface<Container, Type>? getInterface
            <Container=Nothing, Type=Anything>
            (String name, ClosedType<Anything>* types);
    
    "Gets a member interface by name. Returns `null` if not found.
     
     See [[getDeclaredClassOrInterface]] for how the [[Container]] type parameter works."
    throws(class IncompatibleTypeException, 
           "If the specified `Container` or `Type` type arguments are not compatible with the actual result, 
            or if the corresponding member is not a `MemberInterface`.")
    throws(class TypeApplicationException, 
           "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal MemberInterface<Container, Type>? getDeclaredInterface
            <Container=Nothing, Type=Anything>
            (String name, ClosedType<Anything>* types);
    
    "Gets a method by name. Returns `null` if not found.
     
     See [[getClassOrInterface]] for how the [[Container]] type parameter works."
    throws(class IncompatibleTypeException, 
           "If the specified `Type` or `Arguments` type arguments are not compatible with the actual result.")
    throws(class TypeApplicationException, 
           "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal Method<Container, Type, Arguments>? getMethod
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (String name, ClosedType<Anything>* types)
        given Arguments satisfies Anything[];

    "Gets a method by name. Returns `null` if not found.
     
     See [[getDeclaredClassOrInterface]] for how the [[Container]] type parameter works."
    throws(class IncompatibleTypeException, 
           "If the specified `Container`, `Type` or `Arguments` type arguments are not compatible with the actual result.")
    throws(class TypeApplicationException,
           "If the specified closed type argument values are not compatible with the actual result's type parameters.")
    shared formal Method<Container, Type, Arguments>? getDeclaredMethod
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (String name, ClosedType<Anything>* types)
        given Arguments satisfies Anything[];
    
    "Gets an attribute by name. Returns `null` if not found.
     
     See [[getClassOrInterface]] for how the [[Container]] type parameter works."
    throws(class IncompatibleTypeException, 
           "If the specified `Get` or `Set` type arguments are not compatible with the actual result.")
    shared formal Attribute<Container, Get, Set>? getAttribute
            <Container=Nothing, Get=Anything, Set=Nothing>
            (String name);

    "Gets an attribute by name. Returns `null` if not found.
     
     See [[getDeclaredClassOrInterface]] for how the [[Container]] type parameter works."
    throws(class IncompatibleTypeException, 
           "If the specified `Container`, `Get` or `Set` type arguments are not compatible with the actual result.")
    shared formal Attribute<Container, Get, Set>? getDeclaredAttribute
            <Container=Nothing, Get=Anything, Set=Nothing>
            (String name);

    "Gets a list of attributes matching the given container and attribute type, annotated with all the
     specified annotations, which are directly declared on this type."
    since("1.1.0")
    shared formal Attribute<Container, Get, Set>[] getDeclaredAttributes
            <Container=Nothing, Get=Anything, Set=Nothing>
            (ClosedType<Annotation>* annotationTypes);

    "Gets a list of attributes matching the given container and attribute type, annotated with all the
     specified annotations, which are declared on this type or inherited."
    since("1.1.0")
    shared formal Attribute<Container, Get, Set>[] getAttributes
            <Container=Nothing, Get=Anything, Set=Nothing>
            (ClosedType<Annotation>* annotationTypes);

    "Gets a list of methods matching the given container, return and parameter types, annotated with all the
     specified annotations, which are directly declared on this type."
    since("1.1.0")
    shared formal Method<Container, Type, Arguments>[] getDeclaredMethods
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
        given Arguments satisfies Anything[];

    "Gets a list of methods matching the given container, return and parameter types, annotated with all the
     specified annotations, which are declared on this type or inherited."
    since("1.1.0")
    shared formal Method<Container, Type, Arguments>[] getMethods
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
            given Arguments satisfies Anything[];

    "Gets a list of member classes matching the given container, return and parameter types, annotated with all the
     specified annotations, which are directly declared on this type."
    since("1.1.0")
    shared formal MemberClass<Container, Type, Arguments>[] getDeclaredClasses
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
            given Arguments satisfies Anything[];
    
    "Returns the list of shared member classes on this class or interface
     and annotated with all the specified annotations. 
     This includes inherited member classes but not unshared member classes."
    since("1.1.0")
    shared formal MemberClass<Container, Type, Arguments>[] getClasses
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
                given Arguments satisfies Anything[];

    "Returns the list of member interfaces directly declared on this class or interface 
     and annotated with all the specified annotations.
     This includes unshared member interfaces but not inherited member interfaces."
    since("1.1.0")
    shared formal MemberInterface<Container, Type>[] getDeclaredInterfaces
            <Container=Nothing, Type=Anything>
            (ClosedType<Annotation>* annotationTypes);
    
    "Returns the list of shared member interfaces on this class or interface
     and annotated with all the specified annotations. 
     This includes inherited member interfaces but not unshared member interfaces."
    since("1.1.0")
    shared formal MemberInterface<Container, Type>[] getInterfaces
            <Container=Nothing, Type=Anything>
            (ClosedType<Annotation>* annotationTypes);
}

