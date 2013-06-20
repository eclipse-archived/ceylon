"Abstraction of types which support a binary addition
 operator. For numeric types, this is just familiar 
 numeric addition. For strings, it is string 
 concatenation. In general, the addition operation 
 should be a binary associative operation."
//see (`String`, `Numeric`)
by ("Gavin")
shared interface Summable<Other> of Other
        given Other satisfies Summable<Other> {

    "The result of adding the given value to this value. 
     This operation should never perform any kind of 
     mutation upon either the receiving value or the 
     argument value."
    shared formal Other plus(Other other);
    
}