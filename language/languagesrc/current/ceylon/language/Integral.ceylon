shared interface Integral<N>
        satisfies Numeric<N> & Ordinal<N>
        given N satisfies Integral<N> {

    doc "The binary |%| operator"
    shared formal N remainder(N number);

}