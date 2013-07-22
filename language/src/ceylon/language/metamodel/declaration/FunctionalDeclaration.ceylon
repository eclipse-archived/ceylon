shared interface FunctionalDeclaration {
    shared formal FunctionOrAttributeDeclaration[] parameterDeclarations;
    shared formal FunctionOrAttributeDeclaration? getParameterDeclaration(String name);
}