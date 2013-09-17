"A declaration that can have type parameters."
shared interface GenericDeclaration {
    
    "The list of type parameters declared on this generic declaration."
    shared formal TypeParameter[] typeParameterDeclarations;
    
    "Finds a type parameter by name. Returns `null` if not found."
    shared formal TypeParameter? getTypeParameterDeclaration(String name);
}