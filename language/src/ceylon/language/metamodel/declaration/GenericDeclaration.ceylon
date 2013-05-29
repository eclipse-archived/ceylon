shared interface GenericDeclaration {
    
    shared formal TypeParameter[] typeParameters;
    
    shared formal TypeParameter? getTypeParameter(String name);
}