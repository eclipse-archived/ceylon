import ceylon.language.meta.declaration { OpenUnion, OpenType }

native class FreeUnion(caseTypes) satisfies OpenUnion {
    shared actual List<OpenType> caseTypes;
    shared actual native Boolean equals(Object other);
    shared actual native String string;
    shared actual native Integer hash;
}
