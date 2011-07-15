shared interface Integral<Other> of Other
        satisfies Numeric<Other> & Ordinal<Other>
        given Other satisfies Integral<Other> {

    doc "The binary |%| operator"
    shared formal Other remainder(Other number);

}