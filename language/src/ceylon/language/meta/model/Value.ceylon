
shared interface Value<out Type=Anything>
        satisfies ValueModel<Type> {

    shared formal Type get();
}
