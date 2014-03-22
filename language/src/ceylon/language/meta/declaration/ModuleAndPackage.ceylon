import ceylon.language { AnnotationType = Annotation }

"A `module` declaration
 from a `module.ceylon` compilation unit"
shared interface Module 
        satisfies Identifiable & AnnotatedDeclaration {
    
    "The module version."
    shared formal String version;
    
    "The package members of the module."
    shared formal Package[] members;
    
    "The modules this module depends on."
    shared formal Import[] dependencies;
    
    "Finds a package by name. Returns `null` if not found."
    shared formal Package? findPackage(String name);
    
    "Finds a package by name in this module or in its dependencies. Note that all transitive `shared`
     dependencies are searched. Returns `null` if not found."
    shared formal Package? findImportedPackage(String name);
}

"Model of an `import` declaration 
 within a module declaration."
shared interface Import 
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
shared interface Package 
        satisfies Identifiable & AnnotatedDeclaration {
    
    "The module this package belongs to."
    shared formal Module container;

    "True if this package is shared."
    shared formal Boolean shared;
    
    "Returns the list of member declarations that satisfy the given `Kind` type argument."
    shared formal Kind[] members<Kind>() 
            given Kind satisfies NestableDeclaration;
    
    "Returns the list of member declarations that satisfy the given `Kind` type argument and
     that are annotated with the given `Annotation` type argument"
    shared formal Kind[] annotatedMembers<Kind, Annotation>() 
            given Kind satisfies NestableDeclaration
            given Annotation satisfies AnnotationType;

    "Looks up a member declaration by name, provided it satisfies the given `Kind` type
     argument. Returns `null` if no such member matches."
    shared formal Kind? getMember<Kind>(String name) 
            given Kind satisfies NestableDeclaration;

    "The value with the given name. Returns `null` if not found."
    shared formal ValueDeclaration? getValue(String name);

    "The class or interface with the given name. Returns `null` if not found."
    shared formal ClassOrInterfaceDeclaration? getClassOrInterface(String name);

    "The function with the given name. Returns `null` if not found."
    shared formal FunctionDeclaration? getFunction(String name);

    "The type alias with the given name. Returns `null` if not found."
    shared formal AliasDeclaration? getAlias(String name);
}

