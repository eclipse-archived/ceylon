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
    ClassDeclaration
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"A class model represents the model of a Ceylon class that you can inspect.
 
 A class model can be either a toplevel [[Class]] or a member [[MemberClass]].
 "
shared sealed interface ClassModel<out Type=Anything, in Arguments=Nothing>
    satisfies ClassOrInterface<Type> 
    given Arguments satisfies Anything[] {
    
    "The declaration model of this class, 
     which is necessarily a [[ClassDeclaration]]."
    shared formal actual ClassDeclaration declaration;
    
    "A function model for this class's initializer or default constructor,
     or null if this class has constructors but lacks a default constructor."
    since("1.2.0")
    shared formal FunctionModel<Type, Arguments>? defaultConstructor;
    
    "Looks up a constructor by name, 
     Returns `null` if no such constructor matches. 
     This excludes unshared constructors."
    throws(class IncompatibleTypeException, 
        "If the specified type arguments are not 
         compatible with the actual result.")
    since("1.2.0")
    shared formal FunctionModel<Type, Arguments>|ValueModel<Type>? getConstructor
            <Arguments>
            (String name)
                given Arguments satisfies Anything[];
    
    "Looks up a constructor by name, 
     Returns `null` if no such constructor matches. 
     This includes unshared constructors."
    throws(class IncompatibleTypeException, 
        "If the specified type arguments are not 
         compatible with the actual result.")
    since("1.2.0")
    shared formal FunctionModel<Type, Arguments>|ValueModel<Type>? getDeclaredConstructor
            <Arguments>
            (String name)
                given Arguments satisfies Anything[];
    
    "Returns the list of callable constructors directly declared on this class 
     and annotated with all the specified annotations.
     This includes unshared callable constructors."
    since("1.2.0")
    shared formal FunctionModel<Type, Arguments>[] getDeclaredCallableConstructors
            <Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
                given Arguments satisfies Anything[];
    
    "Returns the list of shared callable constructors on this class
     and annotated with all the specified annotations. 
     This does not include unshared callable constructors."
    since("1.2.0")
    shared formal FunctionModel<Type, Arguments>[] getCallableConstructors
            <Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
                given Arguments satisfies Anything[];
    
    "Returns the list of value constructors directly declared on this class 
     and annotated with all the specified annotations.
     This includes unshared constructors."
    since("1.2.0")
    shared formal ValueModel<Type>[] getDeclaredValueConstructors
            (ClosedType<Annotation>* annotationTypes);
    
    "Returns the list of shared value constructors on this class
     and annotated with all the specified annotations. 
     This does not include unshared value constructors."
    since("1.2.0")
    shared formal ValueModel<Type>[] getValueConstructors
            (ClosedType<Annotation>* annotationTypes);


}
