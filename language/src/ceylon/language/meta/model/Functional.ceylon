
import ceylon.language.meta.model {
    ClosedType=Type
}

"Abstraction for models which have a parameter list."
since("1.2.0")
shared sealed interface Functional {
    "The parameter types"
    shared formal ClosedType<>[] parameterTypes;
}

