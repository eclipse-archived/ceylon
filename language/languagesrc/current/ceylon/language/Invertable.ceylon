shared interface Invertable<Inverse> {
    
    doc "The unary |-| operator"
    shared formal Inverse inverse;

    doc "The unary |+| operator"
    shared formal Inverse value;

}