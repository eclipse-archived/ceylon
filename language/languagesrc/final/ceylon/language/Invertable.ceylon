shared interface Invertable<I> 
    given I satisfies Number {
    
    doc "The unary |-| operator"
    shared formal I inverse;

}