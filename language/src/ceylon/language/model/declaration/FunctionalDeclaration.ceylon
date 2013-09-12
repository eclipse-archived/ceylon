shared interface FunctionalDeclaration {
    
    // FIXME: add Boolean annotation
    
    shared formal FunctionOrValueDeclaration[] parameterDeclarations;
    shared formal FunctionOrValueDeclaration? getParameterDeclaration(String name);
}