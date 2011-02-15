shared interface Bits<N>
        satisfies BoundedSequence<Boolean,N> & Equality<Bits<N>> & FixedSlots<Bits<N>>
        given N satisfies Dimension {
}