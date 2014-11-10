import ceylon.language.meta.declaration {
    OpenTypeVariable, TypeParameter
}

native class OpenTvar(declaration) satisfies OpenTypeVariable {
    shared actual TypeParameter declaration;
    shared actual native Boolean equals(Object other);
    string=declaration.qualifiedName;
    hash=string.hash;
}

