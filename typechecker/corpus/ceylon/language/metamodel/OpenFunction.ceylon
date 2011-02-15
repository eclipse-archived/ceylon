shared interface OpenFunction<R, P...> satisfies Function<R, P...> {

    shared formal void intercept( R onInvoke(R proceed(P... args), P... args) )();

}