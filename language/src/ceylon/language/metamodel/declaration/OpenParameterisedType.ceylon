shared interface OpenParameterisedType<out DeclarationType>
    satisfies OpenType
    given DeclarationType satisfies ClassOrInterfaceDeclaration {
    
    shared formal DeclarationType declaration;
    
    shared formal OpenParameterisedType<ClassDeclaration>? superclass;
    
    shared formal OpenParameterisedType<InterfaceDeclaration>[] interfaces;

    shared formal Map<TypeParameter, OpenType> typeArguments;
}
