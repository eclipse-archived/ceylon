import ceylon.language.meta.declaration {
    OpenTypeVariable, TypeParameter
}

native class OpenTvar(declaration) satisfies OpenTypeVariable {
    shared actual TypeParameter declaration;
    shared actual native String string;
    shared actual native Boolean equals(Object other);
    shared actual native Integer hash;
}

