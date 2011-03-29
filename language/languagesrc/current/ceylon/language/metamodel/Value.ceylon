shared interface Value<out T>
        satisfies ValueDeclaration<T> {

    shared formal extension Gettable<T> value;

}