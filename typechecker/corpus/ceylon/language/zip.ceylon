shared BoundedSequence<T,N> zip<T,X,Y,N>(BoundedSequence<X,N> x, BoundedSequence<Y,N> y, T producing(X x, Y y))
        given N satisfies Dimension {
    return from (Bounded<N> i in zeroTo<N>())
                select (producing(x[i],y[i]));
}

shared BoundedSequence<T,N> zip<T,X,Y,N>(BoundedSequence<X,N> x, BoundedSequence<Y,N> y, T producing(X x, Y y))
        given N satisfies Dimension {
    return from (Bounded<N> i in zeroTo<N>())
                select (producing(x[i],y[i]));
}

shared BoundedSequence<T,N> zip<T,X,N>(BoundedSequence<X,N>... lists, T producing(X... x))
        given N satisfies Dimension {
    return from (Bounded<N> i in zeroTo<N>())
                select (producing(from (BoundedSequence<X,N> list in lists) select (list[i])));
}