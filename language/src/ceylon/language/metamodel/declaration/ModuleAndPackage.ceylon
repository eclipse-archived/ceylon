import ceylon.language.metamodel{Annotated}

"Model of a `module` declaration
 from a `module.ceylon` compilation unit"
shared interface Module 
        satisfies Identifiable & Annotated {
    
    "The name of the module."
    shared formal String name;
    
    "The version of the module."
    shared formal String version;
    
    "The package members of the module."
    shared formal Package[] members;
    
    "The modules this module depends on."
    shared formal Import[] dependencies;
}

"Model of an `import` declaration 
 within a module declaration."
shared interface Import 
        satisfies Identifiable & Annotated {
    
    "The name of the imported module."
    shared formal String name;
    
    "The compile-time version of the imported module."
    shared formal String version;
}

"Model of a `package` declaration 
 from a `package.ceylon` compilation unit"
shared interface Package 
        satisfies Identifiable & Annotated {
    
    "The name of the package."
    shared formal String name;
    
    "The module this package belongs to."
    shared formal Module container;
    
    "The members of this package."
    shared formal Kind[] members<Kind>() 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    "The members of this package having a particular annotation."
    shared formal Kind[] annotatedMembers<Kind, Annotation>() 
            given Kind satisfies TopLevelOrMemberDeclaration;

    "The attribute with the given name."
    shared formal AttributeDeclaration? getAttribute(String name);

    "The function with the given name."
    shared formal FunctionDeclaration? getFunction(String name);
}

