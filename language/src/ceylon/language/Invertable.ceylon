"Abstraction of [[additive|Summable]] numeric types which 
 support a unary operation `-x` producing the additive
 inverse of `x`. Every `Invertable` type supports a binary 
 subtraction operation `x-y`.
 
     Integer negativeOne = -1;
     Float delta = x-y;
 
 A concrete class that implements this interface should be a 
 mathematical _group_. That is, it should have an additive 
 identity, denoted `0`, and satisfy:
 
 - `0+x == x+0 == x`
 - `x + -x == 0`
 
 Subtraction must be defined so that it is consistent with
 the additive inverse:
 
 - `x - y == x + -y`"
see (`class Integer`, `class Float`)
by ("Gavin")
shared interface Invertable<Other> of Other
        satisfies Summable<Other>
    given Other satisfies Invertable<Other> {
    
    "The additive inverse of this value."
    shared formal Other negated;
    
    "The difference between this number and the given 
     number. Must produce the value `x + -y`."
    shared default Other minus(Other other) => this + -other;
    
}