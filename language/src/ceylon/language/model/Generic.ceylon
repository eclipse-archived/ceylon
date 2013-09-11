import ceylon.language.model.declaration {
    TypeParameter
}
import ceylon.language.model {
    ClosedType = Type
}

shared interface Generic {
    shared formal Map<TypeParameter, ClosedType<Anything>> typeArguments;
}