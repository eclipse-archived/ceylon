shared sealed interface ConstructorDeclaration 
        satisfies FunctionalDeclaration & NestableDeclaration {
    
    "Whether this constructor declaration is the default constructor"
    shared formal Boolean defaultConstructor;
    
    "The class this constructor constructs"
    shared actual formal ClassDeclaration container;
}