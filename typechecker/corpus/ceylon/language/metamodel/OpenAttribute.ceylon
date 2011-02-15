shared interface OpenAttribute<in X, T>
        satisfies Attribute<X, T> {

    shared formal void intercept<S>( T onGet(S instance, T proceed()) )()
                    given S abstracts X;

}