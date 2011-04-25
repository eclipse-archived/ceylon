shared interface Cloneable<out T>
        given T satisfies Cloneable<T> {
    shared formal T clone;
}