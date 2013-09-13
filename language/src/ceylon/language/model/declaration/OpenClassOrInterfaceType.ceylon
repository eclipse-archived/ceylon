shared interface OpenClassOrInterfaceType
    of OpenClassType | OpenInterfaceType
    satisfies OpenType {
    
    shared formal ClassOrInterfaceDeclaration declaration;
    
    shared formal OpenClassType? superclass;
    
    shared formal OpenInterfaceType[] interfaces;

    shared formal Map<TypeParameter, OpenType> typeArguments;
}
