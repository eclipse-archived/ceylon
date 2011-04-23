shared interface Cloneable<T>
        given T satisfies Cloneable<T> {
    shared formal T clone;
}