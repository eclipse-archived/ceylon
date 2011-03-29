shared interface MutableValue<T>
        satisfies Value<T> {

    shared actual formal Settable<T> value;

}