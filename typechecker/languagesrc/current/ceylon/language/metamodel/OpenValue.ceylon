shared interface OpenValue<T> satisfies Value<T> {

    shared formal void intercept( T onGet(T proceed()) )();

}