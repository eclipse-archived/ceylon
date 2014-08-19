import ceylon.language.meta.declaration {
  OpenClassType, ClassDeclaration, OpenInterfaceType,
  TypeParameter, OpenType
}

shared native class FreeClass(declaration)
        satisfies OpenClassType {
    shared actual ClassDeclaration declaration;
    shared native actual OpenClassType? extendedType;
    shared native actual OpenInterfaceType[] satisfiedTypes;
    shared native actual Map<TypeParameter, OpenType> typeArguments;
    shared native actual Boolean equals(Object other);
    shared native actual String string;
}
