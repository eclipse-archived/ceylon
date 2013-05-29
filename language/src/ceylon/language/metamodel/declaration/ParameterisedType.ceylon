shared interface ParameterisedType<out DeclarationType>
    satisfies OpenType
    given DeclarationType satisfies ClassOrInterfaceDeclaration {
    
    shared formal DeclarationType declaration;
    
    shared formal ParameterisedType<ClassDeclaration>? superclass;
    
    shared formal ParameterisedType<InterfaceDeclaration>[] interfaces;

    shared formal Map<TypeParameter, OpenType> typeArguments;
}
