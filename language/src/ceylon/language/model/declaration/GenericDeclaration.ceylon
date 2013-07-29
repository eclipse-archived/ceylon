shared interface GenericDeclaration {
    
    shared formal TypeParameter[] typeParameterDeclarations;
    
    shared formal TypeParameter? getTypeParameterDeclaration(String name);
}