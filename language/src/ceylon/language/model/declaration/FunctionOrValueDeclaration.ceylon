shared interface FunctionOrValueDeclaration
    of FunctionDeclaration
     | ValueDeclaration
    satisfies NestableDeclaration {
    
    shared formal Boolean parameter;
    
    shared formal Boolean defaulted;
    
    shared formal Boolean variadic;
}