"A function or value declaration."
shared interface FunctionOrValueDeclaration
    of FunctionDeclaration
     | ValueDeclaration
    satisfies NestableDeclaration {
    
    "True if this function or value is a parameter to a [[FunctionalDeclaration]]."
    shared formal Boolean parameter;
    
    "True if this function or value is a parameter and has a default value."
    shared formal Boolean defaulted;
    
    "True if this function or value is a parameter and is variadic (accepts a list of values)."
    shared formal Boolean variadic;
}