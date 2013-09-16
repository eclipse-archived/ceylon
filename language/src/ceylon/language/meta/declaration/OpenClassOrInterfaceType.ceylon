shared interface OpenClassOrInterfaceType
    of OpenClassType | OpenInterfaceType
    satisfies OpenType {
    
    shared formal ClassOrInterfaceDeclaration declaration;
    
    shared formal OpenClassType? extendedType;
    
    shared formal OpenInterfaceType[] satisfiedTypes;

    shared formal Map<TypeParameter, OpenType> typeArguments;
}
