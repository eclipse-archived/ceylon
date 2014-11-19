import ceylon.language.meta.declaration{
    Declaration
}
import ceylon.language.meta.model {
    ClosedType=Type
}
shared sealed interface Functional {
    "The parameter types"
    shared formal ClosedType<Anything>[] parameterTypes;
}

shared sealed interface Bindable<in Container, out Kind> 
        satisfies Kind(Container) {
    
    "Type-unsafe container binding, to be used when the container type is unknown until runtime.
     
     This has the same behaviour as invoking this `Member` directly, but exchanges compile-time type
     safety with runtime checks."
    throws(`class IncompatibleTypeException`, "If the container is not assignable to this member's container")
    shared formal Kind bind(Object container);
}
shared sealed interface Declared {
    shared formal Declaration declaration;
}