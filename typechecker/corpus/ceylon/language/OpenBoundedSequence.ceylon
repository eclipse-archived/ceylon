shared interface OpenBoundedSequence<X,N>
        satisfies BoundedSequence<X,N> & OpenSequence<X>
        given X satisfies Equality<X> {
    shared Settable<X> value(Bounded<N> index);
}