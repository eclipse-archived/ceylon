shared interface Invertable<Inverse> {
    
    doc "The unary |-| operator"
    shared formal Inverse negativeValue;

    doc "The unary |+| operator"
    shared formal Inverse positiveValue;

}