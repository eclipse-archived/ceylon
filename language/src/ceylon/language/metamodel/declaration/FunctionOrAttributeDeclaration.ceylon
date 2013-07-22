shared interface FunctionOrAttributeDeclaration
    of FunctionDeclaration
     | AttributeDeclaration
    satisfies TopLevelOrMemberDeclaration {
    
    // FIXME: add Boolean parameter
    
    shared formal Boolean defaulted;
    
    shared formal Boolean variadic;
}