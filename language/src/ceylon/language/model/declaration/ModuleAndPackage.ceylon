import ceylon.language.model{Annotated}

"Model of a `module` declaration
 from a `module.ceylon` compilation unit"
shared interface Module 
        satisfies Identifiable & AnnotatedDeclaration {
    
    "The version of the module."
    shared formal String version;
    
    "The package members of the module."
    shared formal Package[] members;
    
    "The modules this module depends on."
    shared formal Import[] dependencies;
    
    "Finds a package by name"
    shared formal Package? findPackage(String name);
}

"Model of an `import` declaration 
 within a module declaration."
shared interface Import 
        satisfies Identifiable & Annotated {
    
    "The name of the imported module."
    shared formal String name;
    
    "The compile-time version of the imported module."
    shared formal String version;

    shared formal Boolean shared;

    shared formal Boolean optional;
}

"Model of a `package` declaration 
 from a `package.ceylon` compilation unit"
shared interface Package 
        satisfies Identifiable & AnnotatedDeclaration {
    
    "The module this package belongs to."
    shared formal Module container;

    shared formal Boolean shared;
    
    "The members of this package."
    shared formal Kind[] members<Kind>() 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    "The members of this package having a particular annotation."
    shared formal Kind[] annotatedMembers<Kind, Annotation>() 
            given Kind satisfies TopLevelOrMemberDeclaration;

    "Looks up a member of this package by name and type."
    shared formal Kind? getMember<Kind>(String name) 
            given Kind satisfies TopLevelOrMemberDeclaration;

    "The value with the given name."
    shared formal ValueDeclaration? getValue(String name);

    "The class or interface with the given name."
    shared formal ClassOrInterfaceDeclaration? getClassOrInterface(String name);

    "The function with the given name."
    shared formal FunctionDeclaration? getFunction(String name);

    "The type alias with the given name."
    shared formal AliasDeclaration? getAlias(String name);
}

