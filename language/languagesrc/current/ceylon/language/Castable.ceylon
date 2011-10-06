shared interface Castable<in Types> {
    shared formal CastValue castTo<CastValue>()
            given CastValue satisfies Types;
}
