"Abstraction of numeric types representing scalar values. 
 This type defines operations which can be expressed without 
 reference to the invariant self types `Other` of 
 [[Numeric]], [[Scalar]], and [[Comparable]].
 
 `Number` allows certain operations to be applied to streams
 of heterogeneous numeric types.
 
     value magnitudes = { -1.0, 1, 0.0, 2, 4.0 }.map(Number.magnitude); 
 
 This would not be well-typed for `Scalar.magnitude`, since 
 `Scalar` is an invariant type.
 
 Every concrete class which implements `Number` should 
 also implement `Scalar`. (Unfortunately, this constraint
 cannot be expressed within the type system of the language
 without loss of the covariance of this type.)"
see (`interface Numeric`, `interface Scalar`)
by ("Gavin")
shared interface Number {
    
    "The magnitude of this number."
    see (`value Scalar.magnitude`)
    shared formal Number magnitude;
    
    "The fractional part of this number, after truncation of 
     the integral part. For [[Integral]] numeric types, the 
     fractional part is always zero."
    shared formal Number fractionalPart;
    
    "The integral value of the number after truncation of 
     the fractional part. For [[Integral]] numeric types, 
     the integral value of a number is the number itself."
    shared formal Number wholePart;
    
    "The number, represented as a [[Float]], if such a 
     representation is possible."
    throws (`class OverflowException`,
    "if the number is too large to be represented 
     as a `Float`")
    shared formal Float float;
    
    "The number, represented as an [[Integer]], after 
     truncation of any fractional part if such a 
     representation is possible."
    throws (`class OverflowException`,
    "if the number is too large to be represented 
     as an `Integer`")
    shared formal Integer integer;
    
    "Determine if the number is strictly positive."
    see (`value Scalar.positive`)
    shared formal Boolean positive;
    
    "Determine if the number is strictly negative."
    see (`value Scalar.negative`)
    shared formal Boolean negative;
    
    "The sign of this number: 
     
     - `1` if the number is positive, 
     - `-1` if it is negative, or 
     - `0` if it is zero."
    shared formal Integer sign;
    
}
