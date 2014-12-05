
import ceylon.language.meta.model {
    ClosedType=Type
}
shared sealed interface Functional /*satisfies Applicable<Anything,Arguments>*/{
    "The parameter types"
    shared formal ClosedType<Anything>[] parameterTypes;
}

