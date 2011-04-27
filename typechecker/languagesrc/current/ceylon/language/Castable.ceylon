shared interface Castable<in S> {
    shared formal Y as<Y>()
        given Y satisfies S;
}
