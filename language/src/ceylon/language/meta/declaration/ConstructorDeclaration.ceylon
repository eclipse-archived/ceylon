"A constructor declaration."
shared sealed interface ConstructorDeclaration 
        satisfies FunctionalDeclaration & AnnotatedDeclaration & TypedDeclaration & Contained{
    
    "Whether this constructor declaration is the default constructor"
    shared formal Boolean defaultConstructor;
    
    "The class this constructor constructs"
    shared actual formal ClassDeclaration container;
    
    "True if this declaration is annotated with [[shared|ceylon.language::shared]]."
    shared formal Boolean shared;
    
    // TODO need to know the extended constructor. Should we go via OpenType a la ClassDeclaration.extendedType
    // that would imply adding an OpenConstructorType case to OpenType 
}