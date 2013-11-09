"Abstraction of types which support a unary additive 
 inversion operation `-x`. For a [[numeric type|Numeric]], 
 this should return the negative of the argument value.
 
 Implementations which also satisfy [[Summable]] should 
 respect the following constraint:
 
 - `x + -x == 0`"
see (`class Integer`, `class Float`)
by ("Gavin")
shared interface Invertable<out Inverse> of Inverse
    given Inverse satisfies Invertable<Inverse> {
    
    "The additive inverse of the value, which may be expressed
     as an instance of a wider type."
    shared formal Inverse negativeValue;
    
    "The value itself, expressed as an instance of the
     wider type."
    shared formal Inverse positiveValue;

}