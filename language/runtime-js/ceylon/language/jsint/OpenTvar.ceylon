import ceylon.language.meta.declaration {
    OpenTypeVariable, TypeParameter
}

native class OpenTvar(declaration) satisfies OpenTypeVariable {
    shared actual TypeParameter declaration;
    shared actual native Boolean equals(Object other);
    // WARNING failure to make these lazy causes infinite recursion. I suppose the declaration
    // can refer to us when we get its qualified name?
    string=>declaration.qualifiedName;
    hash=>string.hash;
}

