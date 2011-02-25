shared interface Annotated {

    doc "Return all the annotation values that are
         assignable to the given type."
    shared formal T[] annotations<T>(Type<T> type = Object)
            given T satisfies Object;

}