shared interface OpenClassOrInterfaceType
    satisfies OpenType {
    
    shared formal ClassOrInterfaceDeclaration declaration;
    
    shared formal OpenClassOrInterfaceType? superclass;
    
    shared formal OpenClassOrInterfaceType[] interfaces;

    shared formal Map<TypeParameter, OpenType> typeArguments;
}
