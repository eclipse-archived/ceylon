shared interface OpenMethod<in X, R, P...>
        satisfies Method<in X, R, P...>> {

    shared formal void intercept<S>( R onInvoke(S instance, R proceed(P... args), P... args) )()
                    given S abstracts X;

}