"Abstraction of [[numeric types|Numeric]] that may be raised 
 to a power `x^p`. Note that the type of the exponent may be 
 different to the numeric type which is exponentiated."
see (`class Integer`, `class Float`)
shared interface Exponentiable<This,Other> of This
        satisfies Numeric<This>
        given This satisfies Exponentiable<This,Other> 
        given Other satisfies Numeric<Other> {

    "The result of raising this number to the given power."
    shared formal This power(Other other);
    
} 