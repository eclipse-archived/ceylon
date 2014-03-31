"A declaration which can be contained in a [[Package]] or in another [[NestableDeclaration]].
 
 Functions, values, classes, interfaes and aliases are such declarations."
shared interface NestableDeclaration of FunctionOrValueDeclaration |
                                        ClassOrInterfaceDeclaration |
                                        SetterDeclaration |
                                        AliasDeclaration
        satisfies AnnotatedDeclaration & TypedDeclaration {

    "True if this declaration is annotated with [[actual|ceylon.language::actual]]."
    shared formal Boolean actual;

    "True if this declaration is annotated with [[formal|ceylon.language::formal]]."
    shared formal Boolean formal;

    "True if this declaration is annotated with [[default|ceylon.language::default]]."
    shared formal Boolean default;

    "True if this declaration is annotated with [[shared|ceylon.language::shared]]."
    shared formal Boolean shared;

    "This declaration's package container."
    shared formal Package containingPackage;

    "This declaration's module container."
    shared formal Module containingModule;

    "This declaration's immediate container, which can be either a [[NestableDeclaration]]
     or a [[Package]]."
    shared formal NestableDeclaration|Package container;
    
    "True if this declaration is a toplevel declaration."
    shared formal Boolean toplevel;
}