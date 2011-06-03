shared interface Castable<in Types> {
    shared formal Type as<Type>()
            given Type satisfies Types;
}
