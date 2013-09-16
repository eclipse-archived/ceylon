shared interface FunctionalDeclaration {
    
    shared formal Boolean annotation;
    
    shared formal FunctionOrValueDeclaration[] parameterDeclarations;
    shared formal FunctionOrValueDeclaration? getParameterDeclaration(String name);
}