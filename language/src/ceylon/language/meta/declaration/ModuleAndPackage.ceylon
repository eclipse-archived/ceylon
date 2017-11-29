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
import ceylon.language.meta.model{ ClassOrInterface }

"A `module` declaration
 from a `module.ceylon` compilation unit"
shared sealed interface Module 
        satisfies Identifiable & AnnotatedDeclaration {
    
    "The module version."
    shared formal String version;
    
    "The package members of the module."
    shared formal Package[] members;
    
    "The modules this module depends on."
    shared formal Import[] dependencies;
    
    "Finds a package by name. Returns `null` if not found."
    shared formal Package? findPackage(String name);
    
    "Finds a package by name in this module or in its 
     dependencies. Note that all transitive `shared`
     dependencies are searched. Returns `null` if not found."
    shared formal Package? findImportedPackage(String name);

    "Searches for a resource by its path inside the module."
    since("1.1.0")
    shared formal Resource? resourceByPath(String path);
    
    "Find providers of the given [[service type|service]].
     
     Returns all the service providers accessible to this 
     module that:
     
     * are annotated with the [[service]] annotation, and
     * have the given class or interface as argument to the 
       [[service]] annotation.
     
     The returned service providers will be returned in an 
     unspecified and platform-dependent order.
     
     A provider usually does not provide the underlying 
     functionality itself, but:
     
     * provides sufficient information to allow clients to 
       decide which provider is best able to satisfy its 
       needs, and
     * provides a means to access the underlying functionality 
       (for example, by serving as a factory)."
    shared formal {Service*} findServiceProviders<Service>(
        "The service type."
        ClassOrInterface<Service> service);
}

"Model of an `import` declaration 
 within a module declaration."
shared sealed interface Import 
        satisfies Identifiable & Annotated {
    
    "The name of the imported module."
    shared formal String name;
    
    "The compile-time version of the imported module."
    shared formal String version;

    "True if this imported module is shared."
    shared formal Boolean shared;

    "True if this imported module is optional."
    shared formal Boolean optional;

    "The containing module."
    shared formal Module container;
}

"Model of a `package` declaration 
 from a `package.ceylon` compilation unit"
shared sealed interface Package 
        satisfies Identifiable & AnnotatedDeclaration {
    
    "The module this package belongs to."
    shared formal Module container;

    "True if this package is shared."
    shared formal Boolean shared;
    
    "Returns the list of member declarations that satisfy 
     the given `Kind` type argument."
    shared formal Kind[] members<Kind>() 
            given Kind satisfies NestableDeclaration;
    
    "Returns the list of member declarations that satisfy 
     the given `Kind` type argument and that are annotated 
     with the given `Annotation` type argument"
    shared formal Kind[] annotatedMembers<Kind, Annotation>() 
            given Kind satisfies NestableDeclaration
            given Annotation satisfies AnnotationType;

    "Looks up a member declaration by name, provided it 
     satisfies the given `Kind` type argument. Returns `null` 
     if no such member matches."
    shared formal Kind? getMember<Kind>(String name) 
            given Kind satisfies NestableDeclaration;

    "The value with the given name. Returns `null` if not 
     found."
    shared formal ValueDeclaration? getValue(String name);

    "The class or interface with the given name. Returns 
     `null` if not found."
    shared formal ClassOrInterfaceDeclaration? getClassOrInterface(String name);

    "The function with the given name. Returns `null` if not 
     found."
    shared formal FunctionDeclaration? getFunction(String name);

    "The type alias with the given name. Returns `null` if 
     not found."
    shared formal AliasDeclaration? getAlias(String name);
}

