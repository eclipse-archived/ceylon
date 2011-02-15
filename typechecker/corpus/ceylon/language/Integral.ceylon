shared interface Integral<N>
        satisfies Numeric<N> & Ordinal
        given N satisfies Number {

    doc "The binary |%| operator"
    shared formal N remainder(N number);

}