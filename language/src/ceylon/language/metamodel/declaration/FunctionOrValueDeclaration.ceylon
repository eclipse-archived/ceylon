shared interface FunctionOrValueDeclaration
    of FunctionDeclaration
     | ValueDeclaration
    satisfies TopLevelOrMemberDeclaration {
    
    // FIXME: add Boolean parameter
    
    shared formal Boolean defaulted;
    
    shared formal Boolean variadic;
}