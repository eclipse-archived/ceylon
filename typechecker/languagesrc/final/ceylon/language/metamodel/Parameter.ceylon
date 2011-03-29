shared interface Parameter<out T>
        satisfies Annotated {

    shared formal String name;

    shared formal Type<T> type;

}