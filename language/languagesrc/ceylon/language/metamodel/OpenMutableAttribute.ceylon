shared interface OpenMutableAttribute<in X, T>
        satisfies OpenAttribute<X, T> & MutableAttribute<X, T> {

    shared formal void intercept<S>( void onSet(S instance, void proceed(T value), T value) )()
                    given S abstracts X;

}