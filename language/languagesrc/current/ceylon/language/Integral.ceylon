shared interface Integral<N>
        satisfies Numeric<N> & Ordinal
        given N satisfies Numeric<N> {

    doc "The binary |%| operator"
    shared formal N remainder(N number);

}