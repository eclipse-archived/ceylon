shared interface Summable<Other> of Other
        given Other satisfies Summable<Other> {

    doc "The binary |+| operator"
    shared formal Other plus(Other that);
    
}