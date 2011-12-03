shared interface Subtractable<Other,Inverse> of Other
        satisfies Numeric<Other> & Invertable<Inverse>
        given Other satisfies Subtractable<Other,Inverse> {

    doc "The difference between this number and the given 
         number."
    shared formal Inverse minus(Other other);

} 