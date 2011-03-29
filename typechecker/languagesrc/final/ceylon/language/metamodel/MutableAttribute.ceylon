shared interface MutableAttribute<in X, T>
        satisfies Attribute<X, T> & Member<X, MutableValue<T>> {}