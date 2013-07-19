shared interface FunctionalDeclaration {
    shared formal ParameterDeclaration[] parameterDeclarations;
    shared formal ParameterDeclaration? getParameterDeclaration(String name);
}