import ceylon.language.meta.declaration { OpenIntersection, OpenType }

native class FreeIntersection(satisfiedTypes) satisfies OpenIntersection {
    shared actual List<OpenType> satisfiedTypes;
    shared actual native Boolean equals(Object other);
    shared actual native String string;
    shared actual native Integer hash;
}
