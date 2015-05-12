
import ceylon.language.meta.model {
    ClosedType=Type
}

"Abstraction for models which have a parameter list. That is, 
 [[ClassModel]]s, [[ConstructorModel]]s and [[FunctionModel]]s."
shared sealed interface Functional /*satisfies Applicable<Anything,Arguments>*/{
    "The parameter types"
    shared formal ClosedType<>[] parameterTypes;
}

