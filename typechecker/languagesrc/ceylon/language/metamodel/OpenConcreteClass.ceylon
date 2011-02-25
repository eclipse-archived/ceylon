shared interface OpenConcreteClass<X, P...>
        satisfies ConcreteClass<X> {

    shared formal void intercept(X onInstantiate(X proceed(P... args), P... args))();

}