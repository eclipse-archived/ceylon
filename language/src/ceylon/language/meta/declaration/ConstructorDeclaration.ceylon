shared sealed interface ConstructorDeclaration 
        satisfies FunctionalDeclaration & AnnotatedDeclaration & TypedDeclaration & Contained{
    
    "Whether this constructor declaration is the default constructor"
    shared formal Boolean defaultConstructor;
    
    "The class this constructor constructs"
    shared actual formal ClassDeclaration container;
    
    "True if this declaration is annotated with [[shared|ceylon.language::shared]]."
    shared formal Boolean shared;
    
    /*
    "This declaration's package container."
    shared formal Package containingPackage;
    
    "This declaration's module container."
    shared formal Module containingModule;
    */
    //"This declaration's immediate container, which can be either a [[NestableDeclaration]]
    // or a [[Package]]."
    //shared formal NestableDeclaration|Package container;
}