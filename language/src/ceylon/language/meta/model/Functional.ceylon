
import ceylon.language.meta.model {
    ClosedType=Type
}

"Abstraction for models which have a parameter list."
shared sealed interface Functional {
    "The parameter types"
    shared formal ClosedType<>[] parameterTypes;
}

