shared interface FunctionOrValueDeclaration
    of FunctionDeclaration
     | ValueDeclaration
    satisfies TopLevelOrMemberDeclaration {
    
    shared formal Boolean parameter;
    
    shared formal Boolean defaulted;
    
    shared formal Boolean variadic;
}