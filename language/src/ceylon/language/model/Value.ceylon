
shared interface Value<out Type>
        satisfies ValueModel<Type> {

    shared formal Type get();
}
