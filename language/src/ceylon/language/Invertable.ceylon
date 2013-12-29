"Abstraction of types which support a unary additive 
 inversion operation `-x`. For a [[numeric type|Numeric]], 
 this should return the negative of the argument value.
 
 A concrete class that implements this interface and which 
 also satisfies [[Summable]] should be a mathematical 
 _group_. That is, it should have an additive identity, 
 denoted `0`, and satisfy:
 
 - `0+x == x+0 == x`
 - `x + -x == 0`
 
 Concrete implementations of `Invertable` which are not also
 `Summable` are discouraged."
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