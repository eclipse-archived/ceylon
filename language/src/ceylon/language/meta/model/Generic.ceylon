import ceylon.language.meta.declaration {
    TypeParameter
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"A generic model which has closed type arguments."
shared interface Generic {
    "The map of type parameter declaration to type arguments for this generic model."
    shared formal Map<TypeParameter, ClosedType<Anything>> typeArguments;
}