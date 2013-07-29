shared interface FunctionalDeclaration {
    shared formal FunctionOrValueDeclaration[] parameterDeclarations;
    shared formal FunctionOrValueDeclaration? getParameterDeclaration(String name);
}