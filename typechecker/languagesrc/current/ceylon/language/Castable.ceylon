shared interface Castable<in Types> {
    shared formal CastValue as<CastValue>()
            given CastValue satisfies Types;
}
