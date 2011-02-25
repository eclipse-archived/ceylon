shared interface OpenMutableValue<T>
        satisfies MutableValue<T> & OpenValue<T> {

    shared formal void intercept( void onSet(void proceed(T value), T value) )();

}