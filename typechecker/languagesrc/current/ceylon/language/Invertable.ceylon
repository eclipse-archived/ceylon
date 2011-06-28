shared interface Invertable<Inverse> {
    
    doc "The unary |-| operator"
    shared formal Inverse negative;

    doc "The unary |+| operator"
    shared formal Inverse positive;

}