"Abstraction of numeric types that may be raised to a
 power. Note that the type of the exponent may be
 different to the numeric type which can be 
 exponentiated."
//see (`Integer`, `Float`)
shared interface Exponentiable<This,Other> of This
        satisfies Numeric<This> 
        given This satisfies Exponentiable<This,Other> 
        given Other satisfies Numeric<Other> {

    "The result of raising this number to the given
     power."
    shared formal This power(Other other);
    
} 