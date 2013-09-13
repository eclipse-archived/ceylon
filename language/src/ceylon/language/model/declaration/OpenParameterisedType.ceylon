// FIXME: rename to OpenClassOrInterfaceType?
shared interface OpenParameterisedType
    satisfies OpenType {
    
    shared formal ClassOrInterfaceDeclaration declaration;
    
    shared formal OpenParameterisedType? superclass;
    
    shared formal OpenParameterisedType[] interfaces;

    shared formal Map<TypeParameter, OpenType> typeArguments;
}
