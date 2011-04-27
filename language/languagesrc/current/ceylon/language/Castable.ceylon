shared interface Castable<in S> {
    shared formal T as<T>()
        given T satisfies S;
}
