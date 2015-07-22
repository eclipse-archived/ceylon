"A declaration which can be contained in a [[Package]] or in another [[NestableDeclaration]].
 
 Functions, values, classes, interfaces and aliases are such declarations."
shared sealed interface NestableDeclaration of 
                                        ValueDeclaration |
                                        ClassOrInterfaceDeclaration |
                                        FunctionalDeclaration |
                                        SetterDeclaration |
                                        AliasDeclaration
        satisfies AnnotatedDeclaration & TypedDeclaration & Contained {

    "True if this declaration is annotated with [[actual|ceylon.language::actual]]."
    shared formal Boolean actual;

    "True if this declaration is annotated with [[formal|ceylon.language::formal]]."
    shared formal Boolean formal;

    "True if this declaration is annotated with [[default|ceylon.language::default]]."
    shared formal Boolean default;

    "True if this declaration is annotated with [[shared|ceylon.language::shared]]."
    shared formal Boolean shared;

    
}