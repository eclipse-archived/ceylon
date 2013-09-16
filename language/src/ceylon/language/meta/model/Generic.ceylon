import ceylon.language.meta.declaration {
    TypeParameter
}
import ceylon.language.meta.model {
    ClosedType = Type
}

shared interface Generic {
    shared formal Map<TypeParameter, ClosedType<Anything>> typeArguments;
}